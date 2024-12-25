package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;

import java.util.Optional;

public interface CrudService<ID, DTO extends AbstractDto<ID, DTO>> {
    Optional<ID> save(DTO dto);

    Optional<DTO> findById(ID id);

    void update(ID id, DTO dto);

    void deleteById(ID id);

    boolean exists(ID id);
}
