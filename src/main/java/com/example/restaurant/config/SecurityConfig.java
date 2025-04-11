package com.example.restaurant.config;

import com.example.restaurant.service.UserDetailServiceImpl;
import com.example.restaurant.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

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
    private final String allowedOrigins;

    /**
     * Instantiates a new Security config.
     *
     * @param userDetailService the user detail service
     * @param jwtUtil           the jwt util
     */
// @Autowired
    public SecurityConfig(@Lazy UserDetailServiceImpl userDetailService, JwtUtil jwtUtil, @Value("${origins.url}") String allowedOrigins) {
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
        this.allowedOrigins = allowedOrigins;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Allow all requests to home view & preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**", "/api/user/**", "/api/category/**", "/api/order/**", "/api/product/**", "/home")
                        .permitAll()
                        // Allow all static resources requests
                        .requestMatchers("/css/**", "/js/**", "/upload/**", "/img/**")
                        .permitAll()
                        .requestMatchers("/api/admin/category/**", "/api/admin/order/**", "/api/admin/product/**")
                        .hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers("/api/admin/user/**", "/api/admin/role/**")
                        .hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
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
     * Authentication manager.
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("https://cozy-halva-4c9815.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization",
                "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}