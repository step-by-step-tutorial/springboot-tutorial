package com.tutorial.springboot.restful_web_api.entity;

import java.beans.Transient;
import java.util.Objects;

import static com.tutorial.springboot.restful_web_api.util.JsonUtils.toJsonString;

@SuppressWarnings(value = {"unchecked"})
public abstract class Entity<ID, SELF extends Entity<ID, SELF>> {

    protected ID id;

    protected Integer version = 1;

    public ID id() {
        return id;
    }

    public SELF id(ID id) {
        this.id = id;
        return (SELF) this;
    }

    public Integer version() {
        return version;
    }

    public SELF version(Integer version) {
        this.version = version;
        return (SELF) this;
    }

    @Transient
    public SELF withInitialVersion() {
        this.version = 1;
        return (SELF) this;
    }

    @Transient
    public SELF increaseVersion() {
        version(version() + 1);
        return (SELF) this;
    }

    @Transient
    public SELF updateFrom(SELF newOne) {
        if (!Objects.equals(this.version(), newOne.version())) {
            throw new IllegalStateException(String.format("%s entity [id %s and version %s] do not match with new update [id %s and version %s]", SampleEntity.class.getSimpleName(), id, this.version(), id, version));
        }
        increaseVersion();
        return (SELF) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (SampleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }
}
