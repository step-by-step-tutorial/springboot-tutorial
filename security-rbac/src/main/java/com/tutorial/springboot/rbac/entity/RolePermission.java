package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.*;

@Entity
public class RolePermission extends AbstractEntity<Long, RolePermission> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public Role getRole() {
        return role;
    }

    public RolePermission setRole(Role role) {
        this.role = role;
        return this;
    }

    public Permission getPermission() {
        return permission;
    }

    public RolePermission setPermission(Permission permission) {
        this.permission = permission;
        return this;
    }

    @Transient
    public static RolePermission of(Role role, Permission permission) {
        return new RolePermission()
                .setRole(role)
                .setPermission(permission);
    }

}
