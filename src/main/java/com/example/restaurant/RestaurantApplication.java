package com.example.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Restaurant application.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.restaurant.repository")
@ComponentScan(basePackages = {
        "com.example.restaurant",
        "com.example.restaurant.utils"
})
public class RestaurantApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }
}
