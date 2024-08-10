package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity
public class Permission extends AbstractEntity<Long, Permission> {

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> authorities;

    public String getName() {
        return name;
    }

    public Permission setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public Permission setAuthorities(Set<Role> roles) {
        this.authorities = roles;
        return this;
    }

}
