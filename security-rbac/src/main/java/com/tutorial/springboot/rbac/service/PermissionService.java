package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.repository.PermissionRepository;
import com.tutorial.springboot.rbac.transformer.PermissionTransformer;
import org.springframework.stereotype.Service;

@Service
public class PermissionService extends AbstractService<Long, Permission, PermissionDto> implements CrudService<Long, PermissionDto>, BatchService<Long, PermissionDto> {

    public PermissionService(PermissionRepository repository, PermissionTransformer transformer) {
        super(repository, transformer);
    }
}
