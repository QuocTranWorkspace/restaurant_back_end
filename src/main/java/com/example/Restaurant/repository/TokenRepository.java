package com.example.Restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.Restaurant.model.TokenEntity;
import com.example.Restaurant.model.UserEntity;

@Repository
public interface TokenRepository extends BaseRepository<TokenEntity> {
    TokenEntity findByToken(String token);

    TokenEntity findByUser(UserEntity user);
}
