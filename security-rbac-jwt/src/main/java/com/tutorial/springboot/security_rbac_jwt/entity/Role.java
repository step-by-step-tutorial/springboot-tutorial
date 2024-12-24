package com.tutorial.springboot.security_rbac_jwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Entity
public class Role extends AbstractEntity<Long, Role> implements GrantedAuthority {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50, message = "Name cannot be longer than 50 characters")
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "permission_id"})
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
    public Role updateFrom(Role newOne) {
        super.updateFrom(newOne);
        this.name = newOne.name;
        return this;
    }

    @Override
    public Role updateRelations(Role newOne) {
        var newPermissions = newOne.permissions.stream()
                .filter(permission -> !this.permissions.contains(permission))
                .toList();

        var deletedPermissions = this.permissions.stream()
                .filter(permission -> !newOne.permissions.contains(permission))
                .toList();

        this.permissions.removeAll(deletedPermissions);
        this.permissions.addAll(newPermissions);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
