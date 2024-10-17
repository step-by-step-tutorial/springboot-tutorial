package com.tutorial.springboot.securityoauth2client.service;

import com.tutorial.springboot.securityoauth2client.dto.AbstractDto;

import java.util.List;

public interface AllService<ID, DTO extends AbstractDto<ID, DTO>> {

    List<DTO> getAll();

    void deleteAll();

}
