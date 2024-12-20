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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type User service.
 */
@Service
public class UserService extends BaseService<UserEntity> {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Instantiates a new User service.
     *
     * @param userRepository  the user repository
     * @param userRoleService the user role service
     * @param jwtUtil         the jwt util
     * @param roleService     the role service
     */
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

    /**
     * Find by username user entity.
     *
     * @param userName the username
     * @return the user entity
     */
    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Register response dto.
     *
     * @param signUpData the sign-up data
     * @param roleName   the role name
     * @return the response dto
     */
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

    /**
     * Login response dto.
     *
     * @param loginData the login data
     * @return the response dto
     */
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

    /**
     * Create user dto.
     *
     * @param userDTO    the user dto
     * @param userEntity the user entity
     */
    public void createUserDTO(UserDTO userDTO, UserEntity userEntity) {
        userDTO.setId(userEntity.getId());
        userDTO.setUserName(userEntity.getUsername());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setAddress(userEntity.getAddress());
        List<String> tempRoles = new ArrayList<>();
        for (RoleEntity role : userEntity.getRoles()) {
            tempRoles.add(role.getRoleName());
        }
        userDTO.setRoles(tempRoles);
    }

    /**
     * Binding user data user dto.
     *
     * @param userEntity the user entity
     * @param userDTO    the user dto
     * @return the user dto
     */
    @SneakyThrows
    public UserDTO bindingUserData(UserEntity userEntity, UserDTO userDTO){
        updateIfNotEmpty(userDTO.getUserName(), userEntity::setUserName);
        updateIfNotEmpty(userDTO.getFirstName(), userEntity::setFirstName);
        updateIfNotEmpty(userDTO.getLastName(), userEntity::setLastName);
        updateIfNotEmpty(userDTO.getEmail(), userEntity::setEmail);
        updateIfNotEmpty(userDTO.getPhone(), userEntity::setPhone);
        updateIfNotEmpty(userDTO.getAddress(), userEntity::setAddress);

        for (RoleEntity roleDB: userEntity.getRoles()) {
            UserRole userRole = userRoleService.findByUserId(roleDB.getId());
            userRoleService.delete(userRole);
            userEntity.deleteRole(roleDB);
            roleDB.deleteUser(userEntity);
            roleService.saveOrUpdate(roleDB);
        }
        for (String role : userDTO.getRoles()) {
            RoleEntity roleSave = objectMapper.readValue(role, RoleEntity.class);
            userEntity.addRole(roleSave);
            UserRole userRole = new UserRole();
            userRole.setUserId(userEntity.getId());
            userRole.setRoleId(roleSave.getId());
            userEntity.addRole(roleSave);
            roleSave.addUser(userEntity);
            roleService.saveOrUpdate(roleSave);
            userRoleService.saveOrUpdate(userRole);
        }

        return userDTO;
    }
}
