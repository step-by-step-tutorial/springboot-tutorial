package com.tutorial.springboot.rest_basic.service;

import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.util.List;
import java.util.Optional;

public interface SampleService {
    Optional<Long> insert(SampleDto dto);

    Optional<SampleDto> selectById(Long id);

    void update(Long id, SampleDto dto);

    void deleteById(Long id);

    boolean exists(Long id);

    List<Long> insertBatch(SampleDto... dtos);

    List<SampleDto> selectBatch(Long... identities);

    void deleteBatch(Long... identities);

    List<SampleDto> selectAll();

    void deleteAll();

    List<Long> selectIdentities();
}
