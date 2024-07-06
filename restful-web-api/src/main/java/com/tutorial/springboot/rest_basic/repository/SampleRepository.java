package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.util.List;
import java.util.Optional;

public interface SampleRepository {
    Optional<Long> insert(SampleDto sample);

    Optional<SampleDto> selectById(Long id);

    void update(Long id, SampleDto dto);

    void deleteById(Long id);

    boolean exists(Long id);

    List<Long> insertBatch(SampleDto... samples);

    List<SampleDto> selectBatch(Long... identities);

    void deleteBatch(Long... identities);

    List<SampleDto> selectAll();

    void deleteAll();

    List<Long> selectIdentities();
}
