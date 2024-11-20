package com.example.restaurant.dto.user;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Sign up dto.
 */
@Setter
@Getter
public class SignUpDTO {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;

}
