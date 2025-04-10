package com.example.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
    private final String allowedOrigins;

    public CorsConfig (@Value("${origins.url}") String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin");
    }
}
