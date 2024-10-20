package com.example.restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.restaurant.model.UserEntity;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {

    UserEntity findByUserName(String username);

}