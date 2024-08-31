package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;

import java.util.List;

public interface AllService<ID, DTO extends AbstractDto<ID, DTO>> {

    List<DTO> getAll();

    void deleteAll();

}
