package com.example.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Mvc config.
 */
@Configuration
@EnableWebMvc
public class MVCConfig implements WebMvcConfigurer {
    private final String filePath;
    private final String origins;
    /**
     * Instantiates a new Mvc config.
     */
    public MVCConfig(@Value("${file.path}")String filePath, @Value("${origins.url}")String origins) {
        super();
        this.filePath = filePath;
        this.origins = origins;
    }

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/upload/product/avatar/**")
                .addResourceLocations("file:" + filePath);
    }
}