package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Resource extends AbstractEntity<Long, Resource> {

    @Column(unique = true, nullable = false)
    private String resourceId;

    private String resourceName;

    private String resourceDescription;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "resource_scopes",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id")
    )
    private List<Scope> scopes;

    public String getResourceId() {
        return resourceId;
    }

    public Resource setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Resource setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public Resource setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
        return this;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public Resource setScopes(List<Scope> scopes) {
        this.scopes = scopes;
        return this;
    }
}
