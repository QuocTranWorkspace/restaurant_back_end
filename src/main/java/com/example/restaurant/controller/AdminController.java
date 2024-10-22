package com.example.restaurant.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.SignUpDTO;
import com.example.restaurant.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseDTO signupAdmin(@RequestBody SignUpDTO user) {
        return userService.register(user, "ADMIN");
    }

}
