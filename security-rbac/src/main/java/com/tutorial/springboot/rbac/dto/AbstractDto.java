package com.tutorial.springboot.rbac.dto;

import java.time.LocalDateTime;

@SuppressWarnings("unchecked")
public abstract class AbstractDto<ID, SELF extends AbstractDto<ID, SELF>> {

    private ID id;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;

    private Integer version;

    public ID getId() {
        return id;
    }

    public SELF setId(ID id) {
        this.id = id;
        return (SELF) this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public SELF setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return (SELF) this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SELF setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return (SELF) this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public SELF setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return (SELF) this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SELF setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return (SELF) this;
    }

    public Integer getVersion() {
        return version;
    }

    public SELF setVersion(Integer version) {
        this.version = version;
        return (SELF) this;
    }

}