package com.example.restaurant.service;

import com.example.restaurant.model.UserRole;
import com.example.restaurant.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

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
