package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.LoginDTO;
import com.example.restaurant.dto.user.SignUpDTO;
import com.example.restaurant.dto.user.UserDTO;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseDTO signupUser(@RequestBody SignUpDTO user) {
        return userService.register(user, "USER");
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO reqUser) {
        ResponseDTO response = null;
        try {
            UserEntity user = userService.findByUserName(reqUser.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(reqUser.getUsername(),
                            reqUser.getPassword(), user.getAuthorities()));

            response = userService.login(reqUser);

            return ResponseEntity.ok(response); // Send token in the response body

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/userAuthenticated")
    public ResponseEntity<UserDTO> getMethodName() {
        Object userAuthenticated = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userAuthenticated instanceof UserDetails) {
            UserEntity user = (UserEntity) userAuthenticated;
            if (Boolean.TRUE.equals(user.getStatus())) {
                UserDTO userResponse = new UserDTO();
                userService.createUserDTO(userResponse, user);
                System.out.println("yahalo" + userResponse.getUserName());
                return ResponseEntity.ok(userResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/validateUsername/{username}")
    public ResponseEntity<ResponseDTO> getMethodName(@PathVariable String username) {
        UserEntity user = userService.findByUserName(username);
        UserDTO userResponse = null;
        if (Objects.nonNull(user)) {
            userResponse = new UserDTO();
            userService.createUserDTO(userResponse, user);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "Validating Username", !Objects.nonNull(userResponse)));
    }

}
