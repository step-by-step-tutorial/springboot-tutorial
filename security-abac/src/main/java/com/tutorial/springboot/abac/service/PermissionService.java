package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.dto.PermissionDto;
import com.tutorial.springboot.abac.entity.Permission;
import com.tutorial.springboot.abac.repository.PermissionRepository;
import com.tutorial.springboot.abac.transformer.PermissionTransformer;
import org.springframework.stereotype.Service;

@Service
public class PermissionService extends AbstractService<Long, Permission, PermissionDto> implements CrudService<Long, PermissionDto>, BatchService<Long, PermissionDto> {

    public PermissionService(PermissionRepository repository, PermissionTransformer transformer) {
        super(repository, transformer);
    }
}
