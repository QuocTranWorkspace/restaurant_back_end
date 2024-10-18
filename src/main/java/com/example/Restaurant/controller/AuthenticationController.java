package com.example.Restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Restaurant.dto.user.LoginDTO;
import com.example.Restaurant.dto.user.SignUpDTO;
import com.example.Restaurant.model.UserEntity;
import com.example.Restaurant.service.UserService;
import com.example.Restaurant.utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthenticationController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public String signupUser(@RequestBody SignUpDTO user) {
        return "user/index";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO reqUser) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(reqUser.getUsername(),
                            reqUser.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        UserEntity user = userService.findByUserName(reqUser.getUsername());
        String token = jwtUtil.generateToken(user); // Generate token

        return ResponseEntity.ok(token); // Send token in the response body
    }

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }

}
