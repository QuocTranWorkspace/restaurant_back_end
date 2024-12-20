package com.example.restaurant.repository;

import com.example.restaurant.model.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends BaseRepository<UserEntity> {
    /**
     * Find by username user entity.
     *
     * @param username the username
     * @return the user entity
     */
    UserEntity findByUserName(String username);

    UserEntity findByEmail(String email);
}