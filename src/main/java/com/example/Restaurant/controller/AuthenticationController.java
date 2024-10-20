package com.example.restaurant.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.LoginDTO;
import com.example.restaurant.dto.user.SignUpDTO;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.service.UserService;

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

    @PostMapping("/signup")
    public String signupUser(@RequestBody SignUpDTO user) {
        return "user/index";
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
    public ResponseEntity<UserEntity> getMethodName() {
        Object userAuthenticated = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userAuthenticated instanceof UserDetails) {
            return ResponseEntity.ok((UserEntity) userAuthenticated);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/validateUsername")
    public ResponseEntity<ResponseDTO> getMethodName(@RequestBody SignUpDTO username) {
        System.out.println(username);
        UserEntity userList = userService.findByUserName(username.getUsername());

        return ResponseEntity.ok(new ResponseDTO(200, "Validating Username", !Objects.nonNull(userList)));
    }

}
