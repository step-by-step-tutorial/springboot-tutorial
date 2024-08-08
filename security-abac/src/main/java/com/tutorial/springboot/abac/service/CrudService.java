package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.dto.AbstractDto;

import java.util.Optional;

public interface CrudService<ID, DTO extends AbstractDto<ID, DTO>> {
    Optional<ID> save(DTO dto);

    Optional<DTO> getById(ID id);

    void update(ID id, DTO dto);

    void delete(ID id);

    boolean exists(ID id);
}
