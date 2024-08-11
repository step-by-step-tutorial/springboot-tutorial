package com.tutorial.springboot.rbac.service.impl;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.RolePermission;
import com.tutorial.springboot.rbac.repository.PermissionRepository;
import com.tutorial.springboot.rbac.repository.RolePermissionRepository;
import com.tutorial.springboot.rbac.repository.RoleRepository;
import com.tutorial.springboot.rbac.service.AbstractService;
import com.tutorial.springboot.rbac.service.BatchService;
import com.tutorial.springboot.rbac.service.CrudService;
import com.tutorial.springboot.rbac.transformer.PermissionTransformer;
import com.tutorial.springboot.rbac.transformer.RoleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<Long, Role, RoleDto> implements CrudService<Long, RoleDto>, BatchService<Long, RoleDto> {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PermissionTransformer permissionTransformer;

    public RoleService(RoleRepository repository, RoleTransformer transformer) {
        super(repository, transformer);
    }

    @Override
    protected void afterSave(Role entity, RoleDto dto) {
        if (dto.getPermissions().isEmpty()) {
            return;
        }

        dto.getPermissions()
                .stream()
                .map(permissionTransformer::toEntity)
                .map(permissionRepository::save)
                .map(permission -> RolePermission.of(entity, permission))
                .forEach(rolePermissionRepository::save);
    }
}
