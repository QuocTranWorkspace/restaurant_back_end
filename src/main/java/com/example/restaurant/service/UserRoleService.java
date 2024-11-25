package com.example.restaurant.service;

import com.example.restaurant.model.UserRole;
import com.example.restaurant.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

/**
 * The type User role service.
 */
@Service
public class UserRoleService extends BaseService<UserRole> {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
    @Override
    protected Class<UserRole> clazz() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UserRole findByUserId(int userId) {
        return userRoleRepository.findByUserId(userId);
    }
}
