package com.tutorial.springboot.rest_basic.entity;

import java.beans.Transient;

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
    public SELF increaseVersion() {
        version(version() + 1);
        return (SELF) this;
    }
}
