package com.example.Restaurant.repository;

import com.example.Restaurant.model.UserEntity;

public interface UserRepository extends BaseRepository<UserEntity> {
    UserEntity findByUserName(String username);
}
