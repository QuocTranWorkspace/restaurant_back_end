package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_role")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roleName")
public class RoleEntity extends BaseEntity implements GrantedAuthority {
    @Column(name = "role_name", length = 45, nullable = false)
    private String roleName;

    @Column(name = "role_description", nullable = false)
    private String roleDescription;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private final Set<UserEntity> users = new HashSet<>();

    public void addUser(UserEntity user) {
        users.add(user);
        user.getRoles().add(this);
    }

    public void deleteUser(UserEntity user) {
        users.remove(user);
        user.getRoles().remove(this);
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    @Override
    public String getAuthority() {
        return this.getRoleName();
    }
}