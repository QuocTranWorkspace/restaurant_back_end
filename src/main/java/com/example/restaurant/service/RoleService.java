package com.example.restaurant.service;

import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<RoleEntity> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    protected Class<RoleEntity> clazz() {
        return RoleEntity.class;
    }

    public RoleEntity findByRoleName(String rolename) {
        return roleRepository.findByRoleName(rolename);
    }

}
