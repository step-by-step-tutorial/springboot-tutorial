package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@MappedSuperclass
public abstract class CodeTable<ID, SELF extends CodeTable<ID, SELF>> extends AbstractEntity<ID, SELF> {

    @NotBlank(message = "Name is mandatory and unique")
    @Size(min = 1, max = 50, message = "Name cannot be longer than 50 characters or empty")
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Override
    public SELF updateFrom(SELF newOne) {
        super.updateFrom(newOne);
        this.name = newOne.getName();
        return (SELF) this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var that = (SELF) o;
        return Objects.equals(name, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public SELF setName(String resourceName) {
        this.name = resourceName;
        return (SELF) this;
    }

    public String getDescription() {
        return description;
    }

    public SELF setDescription(String resourceDescription) {
        this.description = resourceDescription;
        return (SELF) this;
    }
}
