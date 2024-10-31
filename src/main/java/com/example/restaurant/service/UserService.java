package com.example.restaurant.service;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.LoginDTO;
import com.example.restaurant.dto.user.SignUpDTO;
import com.example.restaurant.dto.user.UserDTO;
import com.example.restaurant.exception.CustomException;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.model.UserRole;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;

@Service
public class UserService extends BaseService<UserEntity> {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, UserRoleService userRoleService, JwtUtil jwtUtil,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
    }

    @Override
    protected Class<UserEntity> clazz() {
        return UserEntity.class;
    }

    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Transactional
    public ResponseDTO register(SignUpDTO signUpData, String roleName) {
        if (Objects.nonNull(this.findByUserName(signUpData.getUsername()))) {
            throw new CustomException("Username already exist");
        }

        // Default role
        RoleEntity role = roleService.findByRoleName(roleName);

        UserEntity userSave = new UserEntity();
        userSave.setUserName(signUpData.getUsername());
        userSave.setPassword(new BCryptPasswordEncoder(4).encode(signUpData.getPassword()));
        userSave.setFirstName(signUpData.getFirstname());
        userSave.setLastName(signUpData.getLastname());
        userSave.setEmail(signUpData.getEmail());
        userSave.addRole(role);
        this.saveOrUpdate(userSave);

        // Set default role = USER
        UserRole ur = new UserRole();
        ur.setRoleId(role.getId());
        ur.setUserId(userSave.getId());
        userRoleService.saveOrUpdate(ur);

        UserDTO userResponse = new UserDTO();
        createUserDTO(userResponse, userSave);

        return new ResponseDTO(200, "Registered successful", userResponse);
    }

    public ResponseDTO login(LoginDTO loginData) {
        // Find user
        UserEntity user = this.findByUserName(loginData.getUsername());

        if (!Objects.nonNull(user)) {
            throw new CustomException("User is not exist");
        }

        if (!new BCryptPasswordEncoder(4).matches(loginData.getPassword(), user.getPassword())) {
            throw new CustomException("Wrong password");
        }

        // Get token
        String token = jwtUtil.generateToken(user);

        return new ResponseDTO(200, "Logged in successful", token);
    }

    public void createUserDTO(UserDTO userDTO, UserEntity userEntity) {
        userDTO.setId(userEntity.getId());
        userDTO.setUserName(userEntity.getUsername());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setAddress(userEntity.getAddress());
        List<String> tempRoles = new ArrayList<>();
        for (RoleEntity role: userEntity.getRoles()) {
            tempRoles.add(role.getRoleName());
        }
        userDTO.setRoles(tempRoles);
    }

    public UserDTO bindingUserData(UserEntity userEntity, UserDTO userDTO) {
        updateIfNotEmpty(userDTO.getUserName(), userEntity::setUserName);
        updateIfNotEmpty(userDTO.getFirstName(), userEntity::setFirstName);
        updateIfNotEmpty(userDTO.getLastName(), userEntity::setLastName);
        updateIfNotEmpty(userDTO.getEmail(), userEntity::setEmail);
        updateIfNotEmpty(userDTO.getPhone(), userEntity::setPhone);
        updateIfNotEmpty(userDTO.getAddress(), userEntity::setAddress);

        return userDTO;
    }

    private void updateIfNotEmpty(String fieldValue, Consumer<String> setter) {
        if (fieldValue != null && !fieldValue.isEmpty()) {
            setter.accept(fieldValue);
        }
    }

}
