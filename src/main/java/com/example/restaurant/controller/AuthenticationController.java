package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.LoginDTO;
import com.example.restaurant.dto.user.SignUpDTO;
import com.example.restaurant.dto.user.UserDTO;
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

import java.util.Objects;

/**
 * The type Authentication controller.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private AuthenticationManager authenticationManager;

    /**
     * Instantiates a new Authentication controller.
     *
     * @param userService the user service
     */
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Sets authentication manager.
     *
     * @param authenticationManager the authentication manager
     */
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Signup user response dto.
     *
     * @param user the user
     * @return the response dto
     */
    @PostMapping("/register")
    public ResponseDTO signupUser(@RequestBody SignUpDTO user) {
        return userService.register(user, "USER");
    }

    /**
     * Login user response entity.
     *
     * @param reqUser the req user
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO reqUser) {
        ResponseDTO response = null;
        try {
            UserEntity user = userService.findByUserName(reqUser.getUsername());

            if (Objects.nonNull(user)) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(reqUser.getUsername(),
                                reqUser.getPassword(), user.getAuthorities()));

                response = userService.login(reqUser);

                return ResponseEntity.ok(response);
            } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Handle options response entity.
     *
     * @return the response entity
     */
    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }

    /**
     * Gets method name.
     *
     * @return the method name
     */
    @GetMapping("/userAuthenticated")
    public ResponseEntity<UserDTO> getMethodName() {
        Object userAuthenticated = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userAuthenticated instanceof UserDetails) {
            UserEntity user = (UserEntity) userAuthenticated;
            if (Boolean.TRUE.equals(user.getStatus())) {
                UserDTO userResponse = new UserDTO();
                userService.createUserDTO(userResponse, user);
                return ResponseEntity.ok(userResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    /**
     * Validate username response entity.
     *
     * @param username the username
     * @return the response entity
     */
    @GetMapping("/validateUsername/{username}")
    public ResponseEntity<ResponseDTO> validateUsername(@PathVariable String username) {
        UserEntity user = userService.findByUserName(username);
        UserDTO userResponse = null;
        if (Objects.nonNull(user)) {
            userResponse = new UserDTO();
            userService.createUserDTO(userResponse, user);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "Validating Username", !Objects.nonNull(userResponse)));
    }
}
