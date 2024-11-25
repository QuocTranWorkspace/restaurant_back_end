package com.example.restaurant.repository;

import com.example.restaurant.model.UserRole;
import org.springframework.stereotype.Repository;

/**
 * The interface User role repository.
 */
@Repository
public interface UserRoleRepository extends BaseRepository<UserRole> {
    UserRole findByUserId(int userId);
}
