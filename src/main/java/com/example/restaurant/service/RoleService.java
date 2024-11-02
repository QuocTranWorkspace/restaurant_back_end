package com.example.restaurant.service;

import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.repository.RoleRepository;
import org.springframework.stereotype.Service;

/**
 * The type Role service.
 */
@Service
public class RoleService extends BaseService<RoleEntity> {
    private final RoleRepository roleRepository;

    /**
     * Instantiates a new Role service.
     *
     * @param roleRepository the role repository
     */
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    protected Class<RoleEntity> clazz() {
        return RoleEntity.class;
    }

    /**
     * Find by role name role entity.
     *
     * @param roleName the role name
     * @return the role entity
     */
    public RoleEntity findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    /**
     * Binding role data role entity.
     *
     * @param roleEntity the role entity
     * @param roleUpdate the role update
     * @return the role entity
     */
    public RoleEntity bindingRoleData(RoleEntity roleEntity, RoleEntity roleUpdate) {
        updateIfNotEmpty(roleUpdate.getRoleName(), roleEntity::setRoleName);
        updateIfNotEmpty(roleUpdate.getRoleDescription(), roleEntity::setRoleDescription);

        return roleEntity;
    }
}
