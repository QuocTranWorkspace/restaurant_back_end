package com.example.Restaurant.service;

import org.springframework.stereotype.Service;

import com.example.Restaurant.model.TokenEntity;
import com.example.Restaurant.model.UserEntity;
import com.example.Restaurant.repository.TokenRepository;

@Service
public class TokenService extends BaseService<TokenEntity> {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected Class<TokenEntity> clazz() {
        return TokenEntity.class;
    }

    public TokenEntity findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public TokenEntity getToken(UserEntity user) {
        return tokenRepository.findByUser(user);
    }

}
