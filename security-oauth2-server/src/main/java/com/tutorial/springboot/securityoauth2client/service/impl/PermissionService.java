package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2client.entity.Permission;
import com.tutorial.springboot.securityoauth2client.repository.PermissionRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.service.BatchService;
import com.tutorial.springboot.securityoauth2client.service.CrudService;
import com.tutorial.springboot.securityoauth2client.transformer.PermissionTransformer;
import org.springframework.stereotype.Service;

@Service
public class PermissionService extends AbstractService<Long, Permission, PermissionDto> implements CrudService<Long, PermissionDto>, BatchService<Long, PermissionDto> {

    public PermissionService(PermissionRepository repository, PermissionTransformer transformer) {
        super(repository, transformer);
    }
}
