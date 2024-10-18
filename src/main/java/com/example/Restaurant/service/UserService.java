package com.example.Restaurant.service;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Restaurant.dto.ResponseDTO;
import com.example.Restaurant.dto.user.LoginDTO;
import com.example.Restaurant.dto.user.SignUpDTO;
import com.example.Restaurant.exception.CustomException;
import com.example.Restaurant.model.RoleEntity;
import com.example.Restaurant.model.TokenEntity;
import com.example.Restaurant.model.UserEntity;
import com.example.Restaurant.model.UserRole;
import com.example.Restaurant.repository.UserRepository;
import com.example.Restaurant.utils.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class UserService extends BaseService<UserEntity> {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, UserRoleService userRoleService, TokenService tokenService,
            JwtUtil jwtUtil, RoleService roleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.tokenService = tokenService;
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
    public ResponseDTO signup(SignUpDTO signUpData) {
        if (Objects.nonNull(this.findByUserName(signUpData.getUsername()))) {
            throw new CustomException("Username already exist");
        }
        RoleEntity role = roleService.findByRoleName("USER");

        // Create new user instance
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
        ur.setRoleId(2);
        ur.setUserId(userSave.getId());
        userRoleService.saveOrUpdate(ur);

        // Save token
        String token = jwtUtil.generateToken(userSave);
        TokenEntity tokenEntity = new TokenEntity(userSave, token);
        tokenService.saveOrUpdate(tokenEntity);

        return new ResponseDTO("sucess", "Sign up successful");
    }

    public ResponseDTO login(LoginDTO loginData) {
        // Find user
        UserEntity user = this.findByUserName(loginData.getUsername());

        if (Objects.nonNull(user)) {
            throw new CustomException("User is not exist");
        }

        if (!user.getPassword().equals(new BCryptPasswordEncoder(4).encode(loginData.getPassword()))) {
            throw new CustomException("Wrong password");
        }

        // Get token
        TokenEntity token = tokenService.getToken(user);

        if (!Objects.nonNull(token)) {
            throw new CustomException("Token is not exist");
        }

        return new ResponseDTO("sucess", token.getToken());
    }

}
