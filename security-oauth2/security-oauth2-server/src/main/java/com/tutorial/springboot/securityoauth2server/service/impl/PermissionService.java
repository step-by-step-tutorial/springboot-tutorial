package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.repository.rbac.PermissionRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.service.BatchService;
import com.tutorial.springboot.securityoauth2server.service.CrudService;
import com.tutorial.springboot.securityoauth2server.transformer.PermissionTransformer;
import org.springframework.stereotype.Service;

@Service
public class PermissionService extends AbstractService<Long, Permission, PermissionDto> implements CrudService<Long, PermissionDto>, BatchService<Long, PermissionDto> {

    public PermissionService(PermissionRepository repository, PermissionTransformer transformer) {
        super(repository, transformer);
    }
}
