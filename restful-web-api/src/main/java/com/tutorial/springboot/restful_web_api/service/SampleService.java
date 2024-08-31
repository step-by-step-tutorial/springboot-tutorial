package com.tutorial.springboot.restful_web_api.service;

import com.tutorial.springboot.restful_web_api.dto.Page;

import java.util.List;
import java.util.Optional;

public interface SampleService<ID, DTO> {

    Optional<ID> save(DTO dto);

    Optional<DTO> findById(ID id);

    void update(ID id, DTO dto);

    void deleteById(ID id);

    boolean exists(ID id);

    List<ID> batchSave(DTO[] items);

    List<DTO> findByIdentifiers(ID[] identifiers);

    Optional<Page<DTO>> findByPage(int page, int size);

    void batchDelete(ID[] identifiers);

    List<DTO> selectAll();

    void deleteAll();

    List<ID> getIdentifiers();
}
