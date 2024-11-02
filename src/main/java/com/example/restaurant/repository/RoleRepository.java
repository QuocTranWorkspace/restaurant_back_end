package com.example.restaurant.repository;

import com.example.restaurant.model.RoleEntity;
import org.springframework.stereotype.Repository;

/**
 * The interface Role repository.
 */
@Repository
public interface RoleRepository extends BaseRepository<RoleEntity> {
    /**
     * Find by role name role entity.
     *
     * @param rolename the rolename
     * @return the role entity
     */
    RoleEntity findByRoleName(String rolename);
}