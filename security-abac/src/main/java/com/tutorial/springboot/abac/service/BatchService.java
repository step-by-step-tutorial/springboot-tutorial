package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.dto.AbstractDto;

import java.util.List;

public interface BatchService<ID, DTO extends AbstractDto<ID, DTO>> {
    List<ID> saveBatch(DTO... dtos);

    List<DTO> getAll();

    void deleteAll();

    void deleteBatch(ID... identities);
}
