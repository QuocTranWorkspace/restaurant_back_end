package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.UserDTO;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.model.UserRole;
import com.example.restaurant.service.UserRoleService;
import com.example.restaurant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRoleService userRoleService;

    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @GetMapping("/userList")
    public ResponseEntity<ResponseDTO> getUserList() {
        List<UserEntity> userList = userService.findAll();
        List<UserDTO> resUser = new ArrayList<>();
        for (UserEntity u : userList) {
            if (Boolean.TRUE.equals(u.getStatus())) {
                UserDTO userResponse = new UserDTO();
                userService.createUserDTO(userResponse, u);
                resUser.add(userResponse);
            }
        }
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", resUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> getUser(@PathVariable("userId") String userId) {
        UserEntity user = userService.getById(Integer.parseInt(userId));
        UserDTO userResponse = new UserDTO();
        userService.createUserDTO(userResponse, user);
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", userResponse));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable("userId") String id, @RequestBody UserDTO user) {
        UserEntity userUpdate = userService.getById(Integer.parseInt(id));
        UserDTO userResponse = null;
        if (Objects.nonNull(userUpdate) && !Objects.isNull(user)) {
            userResponse = userService.bindingUserData(userUpdate, user);
            userUpdate.setUpdatedDate(new Date());
            userService.saveOrUpdate(userUpdate);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", userResponse));
    }

    @PostMapping("/addUser")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserDTO user) {
        UserEntity userEntity = new UserEntity();
        UserDTO userResponse = null;
        if (Objects.nonNull(user)) {
            userResponse = userService.bindingUserData(userEntity, user);
            userEntity.setPassword(new BCryptPasswordEncoder(4).encode("Abc@123"));
            userService.saveOrUpdate(userEntity);
        }
        for (RoleEntity role: userEntity.getRoles()) {
            UserRole ur = new UserRole();
            ur.setRoleId(role.getId());
            ur.setUserId(userEntity.getId());
            userRoleService.saveOrUpdate(ur);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", userResponse));
    }
}
