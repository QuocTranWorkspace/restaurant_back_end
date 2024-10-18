package com.example.Restaurant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.Restaurant.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // Allow all requests to home view
                        .requestMatchers("/api/auth/**").permitAll()
                        // Allow all static resources requests
                        .requestMatchers("/css/**", "/js/**", "/upload/**", "/img/**")
                        .permitAll()
                        // Admin requests requires to be authenticated with authority
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        // Others requests requires to be authenticated
                        .anyRequest().authenticated())
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

    // @Autowired
    public SecurityConfig(@Lazy UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
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