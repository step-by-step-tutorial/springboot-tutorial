package com.tutorial.springboot.securitydynamicpolicy.service;

import com.tutorial.springboot.securitydynamicpolicy.dto.AbstractDto;

import java.util.List;

public interface AllService<ID, DTO extends AbstractDto<ID, DTO>> {

    List<DTO> findAll();

    void deleteAll();

}
