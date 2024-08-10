package com.tutorial.springboot.rbac.entity;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.Map;

@Entity
public class Resource extends AbstractEntity<Long, Resource> {

    private String resourceName;

    @ElementCollection
    private Map<String, String> attributes;

    public String getResourceName() {
        return resourceName;
    }

    public Resource setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Resource setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }
}
