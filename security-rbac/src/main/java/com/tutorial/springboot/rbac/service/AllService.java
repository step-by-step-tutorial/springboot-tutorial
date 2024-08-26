package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.AbstractDto;

import java.util.List;

public interface AllService<ID, DTO extends AbstractDto<ID, DTO>> {

    List<DTO> getAll();

    void deleteAll();

}
