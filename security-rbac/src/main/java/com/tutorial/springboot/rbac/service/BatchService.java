package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.AbstractDto;

import java.util.List;

public interface BatchService<ID, DTO extends AbstractDto<ID, DTO>> {
    List<ID> saveBatch(List<DTO> dtoList);

    List<DTO> getAll();

    void deleteAll();

    void deleteBatch(List<ID> identities);
}
