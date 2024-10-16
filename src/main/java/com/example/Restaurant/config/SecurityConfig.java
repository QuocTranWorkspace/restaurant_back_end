package com.example.Restaurant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.Restaurant.service.UserDetailServiceImpl;
import com.example.Restaurant.utils.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow all requests to home view
                        .requestMatchers("/", "/home", "/home1").permitAll()
                        // Allow all static resources requests
                        .requestMatchers("/css/**", "/js/**", "/upload/**", "/img/**")
                        .permitAll()
                        // Admin requests requires to be authenticated with authority
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        // Others requests requires to be authenticated
                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(auth, jwtUtil))
                .formLogin(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .build();
    }

    /*
     * Spring will automatically wire the dependencies if there is only one
     * constructor in the class.
     * This is often preferred because it makes the dependency explicit and avoids
     * some of the issues that can arise with field injection.
     */
    private final UserDetailServiceImpl userDetailService;
    private JwtUtil jwtUtil;
    private AuthenticationManager auth;

    public SecurityConfig(@Lazy UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
        this.auth = auth;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setAuth(AuthenticationManager auth) {
        this.auth = auth;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    /**
     * Service configuration and password encode algorithm
     *
     * @param auth auth
     * @throws Exception e
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
    }

}