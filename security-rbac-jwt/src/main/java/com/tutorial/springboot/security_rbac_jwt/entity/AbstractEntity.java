package com.tutorial.springboot.security_rbac_jwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("unchecked")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<ID, SELF extends AbstractEntity<ID, SELF>> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Size(min = 1, message = "Metadata [create_by] cannot be empty or null")
    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    @NotNull(message = "Version cannot be null")
    @Min(value = 0, message = "Version cannot be negative")
    @Column(nullable = false)
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
    public SELF updateFrom(SELF newOne) {
        if (!Objects.equals(this.version, newOne.getVersion())) {
            throw new IllegalStateException("The given entity has a different version. Cannot update.");
        }

        updateRelations(newOne);

        return (SELF) this;
    }

    @Transient
    public SELF updateRelations(SELF newOne) {
        return (SELF) this;
    }
}