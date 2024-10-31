package com.example.restaurant.service;

import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

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

    public RoleEntity findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public RoleEntity bindingRoleData(RoleEntity roleEntity, RoleEntity roleUpdate) {
        updateIfNotEmpty(roleUpdate.getRoleName(), roleEntity::setRoleName);
        updateIfNotEmpty(roleUpdate.getRoleDescription(), roleEntity::setRoleDescription);

        return roleEntity;
    }

    private void updateIfNotEmpty(String fieldValue, Consumer<String> setter) {
        if (fieldValue != null && !fieldValue.isEmpty()) {
            setter.accept(fieldValue);
        }
    }
}
