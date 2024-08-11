package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Role extends AbstractEntity<Long, Role> implements GrantedAuthority {

    @NotBlank(message = "Role is mandatory")
    @Size(max = 50, message = "Role cannot be longer than 50 characters")
    private String authority;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RolePermission> rolePermissions = new ArrayList<>();

    @Override
    public String getAuthority() {
        return authority;
    }

    public Role setAuthority(String name) {
        this.authority = name;
        return this;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public Role setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
        return this;
    }

    public List<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    @Override
    public void updateFrom(Role newOne) {
        super.updateFrom(newOne);
        this.authority = newOne.authority;
    }
}
