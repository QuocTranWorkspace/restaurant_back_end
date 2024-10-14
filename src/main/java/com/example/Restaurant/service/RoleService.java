package com.example.Restaurant.service;

import org.springframework.stereotype.Service;

import com.example.Restaurant.model.RoleEntity;
import com.example.Restaurant.repository.RoleRepository;

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
