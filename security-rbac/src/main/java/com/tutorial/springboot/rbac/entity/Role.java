package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.*;
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

    @ManyToMany(mappedBy = "authorities")
    private List<User> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions = new ArrayList<>();

    @Override
    public String getAuthority() {
        return authority;
    }

    public Role setAuthority(String name) {
        this.authority = name;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public Role setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public void updateFrom(Role newOne) {
        super.updateFrom(newOne);
        this.authority = newOne.authority;
    }
}
