package com.example.restaurant.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type User dto.
 */
@Setter
@Getter
public class UserDTO {

    private int id;

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String address;

    private List<String> roles;

}
