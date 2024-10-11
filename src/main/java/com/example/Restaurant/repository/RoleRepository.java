package com.example.Restaurant.repository;

import com.example.Restaurant.model.RoleEntity;

public interface RoleRepository extends BaseRepository<RoleEntity> {
    RoleEntity findByRoleName(String rolename);
}
