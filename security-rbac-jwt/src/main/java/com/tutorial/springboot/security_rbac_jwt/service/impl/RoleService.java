package com.tutorial.springboot.security_rbac_jwt.service.impl;

import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.repository.PermissionRepository;
import com.tutorial.springboot.security_rbac_jwt.repository.RoleRepository;
import com.tutorial.springboot.security_rbac_jwt.service.AbstractService;
import com.tutorial.springboot.security_rbac_jwt.service.BatchService;
import com.tutorial.springboot.security_rbac_jwt.service.CrudService;
import com.tutorial.springboot.security_rbac_jwt.transformer.RoleTransformer;
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
        var permissions = permissionRepository.findOrBatchSave(entity.getPermissions());
        entity.getPermissions().clear();
        entity.getPermissions().addAll(permissions);
    }


}
