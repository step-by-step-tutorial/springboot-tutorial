package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.repository.rbac.PermissionRepository;
import com.tutorial.springboot.securityoauth2server.repository.rbac.RoleRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.service.BatchService;
import com.tutorial.springboot.securityoauth2server.service.CrudService;
import com.tutorial.springboot.securityoauth2server.transformer.RoleTransformer;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<Long, Role, RoleDto> implements CrudService<Long, RoleDto>, BatchService<Long, RoleDto> {

    private final PermissionRepository permissionRepository;

    public RoleService(
            RoleRepository repository,
            PermissionRepository permissionRepository,
            RoleTransformer transformer
    ) {
        super(repository, transformer);
        this.permissionRepository = permissionRepository;
    }

    @Override
    protected void beforeSave(RoleDto dto, Role entity) {
        var permissions = permissionRepository.findOrSaveAll(entity.getPermissions());
        entity.getPermissions().clear();
        entity.getPermissions().addAll(permissions);
    }

}
