package com.tutorial.springboot.securityoauth2client.service;

import com.tutorial.springboot.securityoauth2client.dto.AbstractDto;

import java.util.Optional;

public interface CrudService<ID, DTO extends AbstractDto<ID, DTO>> {
    Optional<ID> save(DTO dto);

    Optional<DTO> getById(ID id);

    void update(ID id, DTO dto);

    void deleteById(ID id);

    boolean exists(ID id);
}
