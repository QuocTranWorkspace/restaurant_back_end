package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService     the user service
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Update profile response entity.
     *
     * @param id   the id
     * @param user the user
     * @return the response entity
     */
    @PostMapping("/profile/{userId}")
    public ResponseEntity<ResponseDTO> updateProfile(@PathVariable("userId") String id, @RequestBody UserEntity user) {
        UserEntity userUpdate = userService.getById(Integer.parseInt(id));
        userUpdate.setUserName(user.getUsername());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setFirstName(user.getFirstName());
        userUpdate.setLastName(user.getLastName());
        userUpdate.setPhone(user.getPhone());
        userUpdate.setAddress(user.getAddress());
        userUpdate.setPassword(new BCryptPasswordEncoder(4).encode(user.getPassword()));

        userService.saveOrUpdate(userUpdate);

        return ResponseEntity.ok(new ResponseDTO(200, "update ok", user));
    }
}
