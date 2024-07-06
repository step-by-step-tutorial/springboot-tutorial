package com.tutorial.springboot.rest_basic.entity;

public interface Entity<T> {
    T id();

    Entity<T> id(T id);

    Integer version();

    Entity<T> increaseVersion();
}
