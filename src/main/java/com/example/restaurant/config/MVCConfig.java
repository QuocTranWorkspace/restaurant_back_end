package com.example.restaurant.config;

import jakarta.servlet.http.HttpServletRequest;
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

    /**
     * Instantiates a new Mvc config.
     *
     * @param filePath the file path
     */
    public MVCConfig(@Value("${file.path}")String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/upload/product/avatar/**")
                .addResourceLocations("file:" + filePath);
    }
}