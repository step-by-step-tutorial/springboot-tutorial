package com.tutorial.springboot.restful_web_api.repository;

import com.tutorial.springboot.restful_web_api.entity.Entity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface SampleRepository<T, E extends Entity<T, E>> {

    Optional<T> insert(E entity);

    Optional<E> selectById(T id);

    void update(T id, E newOne);

    void deleteById(T id);

    boolean exists(T id);

    List<T> insertBatch(E[] entities);

    Stream<E> selectBatch(T[] identifiers);

    void deleteBatch(T[] identifiers);

    Stream<E> selectAll();

    void deleteAll();

    Stream<T> selectIdentifiers();

    Stream<E> selectByPage(int page, int size);

    int count();
}
