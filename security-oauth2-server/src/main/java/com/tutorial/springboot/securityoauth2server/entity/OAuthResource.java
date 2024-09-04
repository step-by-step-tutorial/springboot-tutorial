package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class OAuthResource extends AbstractEntity<Long, OAuthResource> {

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

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
    }
}
