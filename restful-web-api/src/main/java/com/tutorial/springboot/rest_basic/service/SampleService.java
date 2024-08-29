package com.tutorial.springboot.rest_basic.service;

import com.tutorial.springboot.rest_basic.dto.Page;

import java.util.List;
import java.util.Optional;

public interface SampleService<ID, DTO> {
    Optional<ID> save(DTO dto);

    Optional<DTO> findById(ID id);

    void update(ID id, DTO dto);

    void deleteById(ID id);

    boolean exists(ID id);

    List<ID> batchSave(DTO... items);

    List<DTO> findByIdentities(ID... identities);

    Page<DTO> findBatch(int page, int size);

    void batchDelete(ID... identities);

    List<DTO> selectAll();

    void deleteAll();

    List<ID> getIdentities();
}
