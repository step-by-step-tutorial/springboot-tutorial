package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BatchService<ID, DTO extends AbstractDto<ID, DTO>> {
    List<ID> saveBatch(List<DTO> dtoList);

    Page<DTO> getBatch(Pageable pageable);

    List<DTO> getBatch(List<ID> identities);

    void deleteBatch(List<ID> identities);
}
