package com.example.restaurant.repository;

import com.example.restaurant.model.RoleEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity> {

    RoleEntity findByRoleName(String rolename);

}