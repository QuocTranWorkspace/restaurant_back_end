package com.example.Restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.Restaurant.model.UserEntity;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {

    UserEntity findByUserName(String username);

}