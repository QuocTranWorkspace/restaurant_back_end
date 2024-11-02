package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.SignUpDTO;
import com.example.restaurant.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin controller.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    /**
     * Instantiates a new Admin controller.
     *
     * @param userService the user service
     */
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Signup admin response dto.
     *
     * @param user the user
     * @return the response dto
     */
    @PostMapping("/register")
    public ResponseDTO signupAdmin(@RequestBody SignUpDTO user) {
        return userService.register(user, "ADMIN");
    }
}
