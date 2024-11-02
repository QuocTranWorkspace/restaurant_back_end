package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Role entity.
 */
@Entity
@Table(name = "tbl_role")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roleName")
public class RoleEntity extends BaseEntity implements GrantedAuthority {
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private final Set<UserEntity> users = new HashSet<>();
    @Column(name = "role_name", length = 45, nullable = false)
    private String roleName;
    @Column(name = "role_description", nullable = false)
    private String roleDescription;

    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(UserEntity user) {
        users.add(user);
        user.getRoles().add(this);
    }

    /**
     * Delete user.
     *
     * @param user the user
     */
    public void deleteUser(UserEntity user) {
        users.remove(user);
        user.getRoles().remove(this);
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public Set<UserEntity> getUsers() {
        return users;
    }

    /**
     * Gets role name.
     *
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets role name.
     *
     * @param roleName the role name
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Gets role description.
     *
     * @return the role description
     */
    public String getRoleDescription() {
        return roleDescription;
    }

    /**
     * Sets role description.
     *
     * @param roleDescription the role description
     */
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    @Override
    public String getAuthority() {
        return this.getRoleName();
    }
}