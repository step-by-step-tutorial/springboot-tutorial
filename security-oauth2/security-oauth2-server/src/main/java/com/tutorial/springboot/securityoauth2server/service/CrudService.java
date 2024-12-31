package com.tutorial.springboot.securityoauth2server.service;

import com.tutorial.springboot.securityoauth2server.dto.AbstractDto;

import java.util.Optional;

public interface CrudService<ID, DTO extends AbstractDto<ID, DTO>> {
    Optional<ID> save(DTO dto);

    Optional<DTO> findById(ID id);

    void update(ID id, DTO dto);

    void deleteById(ID id);

    boolean exists(ID id);
}
