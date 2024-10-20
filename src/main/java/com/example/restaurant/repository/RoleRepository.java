package com.example.restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.restaurant.model.RoleEntity;

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity> {

    RoleEntity findByRoleName(String rolename);

}