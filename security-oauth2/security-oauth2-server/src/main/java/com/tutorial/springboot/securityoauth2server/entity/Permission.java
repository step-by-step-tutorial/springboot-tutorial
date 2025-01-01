package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Audited
public class Permission extends CodeTable<Long, Permission> {

    @NotAudited
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    public List<Role> getRoles() {
        return roles;
    }

    public Permission setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }
}
