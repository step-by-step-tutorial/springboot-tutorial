package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Permission extends AbstractEntity<Long, Permission> {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
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
    public void updateFrom(Permission newOne) {
        super.updateFrom(newOne);
        this.name = newOne.name;
    }
}
