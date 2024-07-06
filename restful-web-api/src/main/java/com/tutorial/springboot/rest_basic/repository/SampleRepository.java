package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.entity.Entity;

import java.util.Optional;
import java.util.stream.Stream;

public interface SampleRepository<T, E extends Entity<T>> {
    Optional<T> insert(E entity);

    Optional<E> selectById(T id);

    void update(E entity, T id, Integer version);

    void deleteById(T id);

    boolean exists(T id);

    Stream<T> insertBatch(E... entities);

    Stream<E> selectBatch(T... identities);

    void deleteBatch(T... identities);

    Stream<E> selectAll();

    void deleteAll();

    Stream<T> selectIdentities();
}
