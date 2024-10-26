package com.example.restaurant.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.UserDTO;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.service.RoleService;
import com.example.restaurant.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/userList")
    public ResponseEntity<ResponseDTO> getUserList() {
        List<UserEntity> userList = userService.findAll();
        List<UserDTO> resUser = new ArrayList<>();
        for (UserEntity u : userList) {
            if (Boolean.TRUE.equals(u.getStatus())) {
                UserDTO ures = new UserDTO();
                ures.setUserName(u.getUsername());
                ures.setFirstName(u.getFirstName());
                ures.setLastName(u.getLastName());
                ures.setEmail(u.getEmail());
                ures.setPhone(u.getPhone());
                ures.setAddress(u.getAddress());
                List<String> tempRoles = new ArrayList<>();
                for (RoleEntity role : u.getRoles()) {
                    tempRoles.add(role.getRoleName());
                }
                ures.setRoles(tempRoles);
                resUser.add(ures);
            }
        }
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", resUser));
    }

    @GetMapping("/roleList")
    public ResponseEntity<ResponseDTO> getMethodName() {
        List<RoleEntity> roleList = roleService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", roleList));
    }

}
