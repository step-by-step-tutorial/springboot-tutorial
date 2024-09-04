package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Scope extends AbstractEntity<Long, Scope> {

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public Scope setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Scope setDescription(String description) {
        this.description = description;
        return this;
    }
}
