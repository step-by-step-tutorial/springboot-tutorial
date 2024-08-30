package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.entity.Entity;

import java.util.Optional;
import java.util.stream.Stream;

public interface SampleRepository<T, E extends Entity<T, E>> {

    Optional<T> insert(E entity);

    Optional<E> selectById(T id);

    void update(T id, E newOne);

    void deleteById(T id);

    boolean exists(T id);

    Stream<T> insertBatch(E[] entities);

    Stream<E> selectBatch(T[] identifiers);

    void deleteBatch(T[] identifiers);

    Stream<E> selectAll();

    void deleteAll();

    Stream<T> selectIdentifiers();

    Stream<E> selectByPage(int page, int size);

    int count();
}
