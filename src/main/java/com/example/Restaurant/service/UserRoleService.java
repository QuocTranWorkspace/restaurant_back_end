package com.example.Restaurant.service;

import org.springframework.stereotype.Service;

import com.example.Restaurant.model.UserRole;
import com.example.Restaurant.repository.UserRoleRepository;

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

}
