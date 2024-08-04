package com.tutorial.springboot.abac.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> authorities;

    public Long getId() {
        return id;
    }

    public Permission setId(Long id) {
        this.id = id;
        return this;
    }

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
