package com.example.Restaurant.service;

import org.springframework.stereotype.Service;

import com.example.Restaurant.model.UserEntity;
import com.example.Restaurant.repository.UserRepository;

@Service
public class UserService extends BaseService<UserEntity> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected Class<UserEntity> clazz() {
        return UserEntity.class;
    }

    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
