package com.tutorial.springboot.rest_basic.dao;

import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.util.List;
import java.util.Optional;

public interface SampleRepository {
    Optional<Long> insert(SampleDto sample);

    Optional<SampleDto> selectById(Long id);

    void update(Long id, SampleDto dto);

    void deleteById(Long id);

    List<Long> insertAll(SampleDto... samples);

    List<SampleDto> selectAll(Long... identities);

    List<SampleDto> selectAll();

    void deleteAll(Long... identities);

    void truncate();

    List<Long> identities();

    boolean exists(Long id);
}
