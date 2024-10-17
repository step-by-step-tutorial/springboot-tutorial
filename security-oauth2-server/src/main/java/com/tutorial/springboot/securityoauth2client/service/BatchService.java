package com.tutorial.springboot.securityoauth2client.service;

import com.tutorial.springboot.securityoauth2client.dto.AbstractDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BatchService<ID, DTO extends AbstractDto<ID, DTO>> {
    List<ID> saveBatch(List<DTO> dtoList);

    Page<DTO> getBatch(Pageable pageable);

    void deleteBatch(List<ID> identities);
}
