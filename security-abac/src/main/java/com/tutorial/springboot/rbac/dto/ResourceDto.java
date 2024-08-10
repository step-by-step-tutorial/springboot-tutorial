package com.tutorial.springboot.rbac.dto;

import java.util.Map;

public class ResourceDto extends AbstractDto<Long, ResourceDto> {

    private String resourceName;

    private Map<String, String> attributes;

    public String getResourceName() {
        return resourceName;
    }

    public ResourceDto setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public ResourceDto setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }
}
