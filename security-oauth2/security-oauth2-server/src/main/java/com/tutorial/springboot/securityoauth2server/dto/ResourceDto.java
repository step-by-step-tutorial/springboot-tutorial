package com.tutorial.springboot.securityoauth2server.dto;

import java.util.List;

public class ResourceDto extends AbstractDto<Long, ResourceDto> {
    private String resourceId;
    private String resourceName;
    private String resourceDescription;
    private List<String> scopes;

    public String getResourceId() {
        return resourceId;
    }

    public ResourceDto setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public ResourceDto setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public ResourceDto setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public ResourceDto setScopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }
}
