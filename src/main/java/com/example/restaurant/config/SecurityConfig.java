package com.example.restaurant.config;

import com.example.restaurant.service.UserDetailServiceImpl;
import com.example.restaurant.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Security config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*
     * Spring will automatically wire the dependencies if there is only one
     * constructor in the class.
     * This is often preferred because it makes the dependency explicit and avoids
     * some of the issues that can arise with field injection.
     */
    private final UserDetailServiceImpl userDetailService;
    private final JwtUtil jwtUtil;

    /**
     * Instantiates a new Security config.
     *
     * @param userDetailService the user detail service
     * @param jwtUtil           the jwt util
     */
// @Autowired
    public SecurityConfig(@Lazy UserDetailServiceImpl userDetailService, JwtUtil jwtUtil) {
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Allow all requests to home view
                        .requestMatchers("/api/auth/**", "/api/**", "/home")
                        .permitAll()
                        // Allow all static resources requests
                        .requestMatchers("/css/**", "/js/**", "/upload/**", "/img/**")
                        .permitAll()
                        .requestMatchers("/admin/**")
                        .hasAnyRole("ADMIN"))
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    /**
     * Authentication manager authentication manager.
     *
     * @param http the http
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}