package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type User entity.
 */
@Setter
@Getter
@Entity
@Table(name = "tbl_user")
public class UserEntity extends BaseEntity implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "tbl_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private final Set<RoleEntity> roles = new HashSet<>();
    @Column(name = "user_name", length = 45, nullable = false)
    private String userName;
    @Column(name = "password", length = 100, nullable = false)
    @Length(min = 6, message = "Password is too weak")
    private String password;
    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;
    @Column(name = "email", length = 45, nullable = false)
    @Email(message = "Invalid Email")
    private String email;
    @Column(name = "phone", length = 100)
    private String phone;
    @Column(name = "address", length = 100)
    private String address;

    /**
     * Add role.
     *
     * @param role the role
     */
    public void addRole(RoleEntity role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    /**
     * Delete role.
     *
     * @param role the role
     */
    public void deleteRole(RoleEntity role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public @Email(message = "Invalid Email") String getEmail() {
        return email;
    }

    // User detail for authentication
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Add role-based authorities
        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}