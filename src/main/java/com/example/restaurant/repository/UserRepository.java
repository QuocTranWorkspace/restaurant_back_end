package com.example.restaurant.repository;

import com.example.restaurant.model.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {
    UserEntity findByUserName(String username);
}