package com.tutorial.springboot.security_rbac_jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Permission extends AbstractEntity<Long, Permission> {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50, message = "Name cannot be longer than 50 characters or empty")
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Permission setName(String name) {
        this.name = name;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Permission setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public Permission updateFrom(Permission newOne) {
        super.updateFrom(newOne);
        this.name = newOne.name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Permission) o;
        return Objects.equals(name, that.name);
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
