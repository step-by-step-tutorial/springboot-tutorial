package com.tutorial.springboot.security_rbac_jwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
public class Role extends AbstractEntity<Long, Role> implements GrantedAuthority {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions = new ArrayList<>();

    @Override
    public String getAuthority() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
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
        this.name = newOne.name;
        var mergedPermission = newOne.permissions
                .stream()
                .map(newPermission ->
                        this.permissions.stream()
                                .filter(existingPermission -> existingPermission.getName().equals(newPermission.getName()))
                                .findFirst()
                                .orElse(newPermission))
                .collect(toList());

        this.permissions.clear();
        this.permissions.addAll(mergedPermission);
    }
}
