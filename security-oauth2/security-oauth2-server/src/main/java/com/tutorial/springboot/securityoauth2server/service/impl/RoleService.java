package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.repository.PermissionRepository;
import com.tutorial.springboot.securityoauth2server.repository.RoleRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.service.BatchService;
import com.tutorial.springboot.securityoauth2server.service.CrudService;
import com.tutorial.springboot.securityoauth2server.transformer.RoleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<Long, Role, RoleDto> implements CrudService<Long, RoleDto>, BatchService<Long, RoleDto> {

    @Autowired
    private PermissionRepository permissionRepository;

    public RoleService(RoleRepository repository, RoleTransformer transformer) {
        super(repository, transformer);
    }

    @Override
    protected void beforeSave(RoleDto dto, Role entity) {
        var permissions = entity.getPermissions()
                .stream()
                .map(permission -> {
                    if (permissionRepository.existsByName(permission.getName())) {
                        return permissionRepository.findByName(permission.getName()).get();
                    } else {
                        return permission;
                    }
                })
                .toList();
        entity.setPermissions(permissions);
    }
}
