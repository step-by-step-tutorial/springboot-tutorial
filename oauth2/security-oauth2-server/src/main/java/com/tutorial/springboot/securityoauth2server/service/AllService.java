package com.tutorial.springboot.securityoauth2server.service;

import com.tutorial.springboot.securityoauth2server.dto.AbstractDto;

import java.util.List;

public interface AllService<ID, DTO extends AbstractDto<ID, DTO>> {

    List<DTO> getAll();

    void deleteAll();

}
