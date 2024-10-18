package com.example.Restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.Restaurant.model.RoleEntity;

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity> {

    RoleEntity findByRoleName(String rolename);

}