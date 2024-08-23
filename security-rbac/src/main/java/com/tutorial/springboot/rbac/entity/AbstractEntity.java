package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuppressWarnings("unchecked")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<ID, SELF extends AbstractEntity<ID, SELF>> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;

    @Version
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

    @Transient
    public void updateFrom(SELF newOne) {
        this.updatedBy = newOne.getUpdatedBy();
        this.updatedAt = newOne.getUpdatedAt();
        this.version = newOne.getVersion();
    }

}