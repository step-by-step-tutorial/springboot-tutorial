package com.tutorial.springboot.security_rbac_jwt.service.impl;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.repository.PermissionRepository;
import com.tutorial.springboot.security_rbac_jwt.service.AbstractService;
import com.tutorial.springboot.security_rbac_jwt.service.BatchService;
import com.tutorial.springboot.security_rbac_jwt.service.CrudService;
import com.tutorial.springboot.security_rbac_jwt.transformer.PermissionTransformer;
import org.springframework.stereotype.Service;

@Service
public class PermissionService extends AbstractService<Long, Permission, PermissionDto> implements CrudService<Long, PermissionDto>, BatchService<Long, PermissionDto> {

    public PermissionService(PermissionRepository repository, PermissionTransformer transformer) {
        super(repository, transformer);
    }
}
