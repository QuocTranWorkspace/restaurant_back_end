package com.example.Restaurant.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_role")
public class RoleEntity extends BaseEntity implements GrantedAuthority {
    @Column(name = "role_name", length = 45, nullable = false)
    private String roleName;

    @Column(name = "role_description", nullable = false)
    private String roleDescription;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "roles")
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
