package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.AbstractDto;

import java.util.List;

public interface BatchService<ID, DTO extends AbstractDto<ID, DTO>> {
    List<ID> saveBatch(DTO... dtos);

    List<DTO> getAll();

    void deleteAll();

    void deleteBatch(ID... identities);
}