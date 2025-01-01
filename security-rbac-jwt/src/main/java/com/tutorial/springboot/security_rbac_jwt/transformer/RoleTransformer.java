package com.tutorial.springboot.security_rbac_jwt.transformer;

import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class RoleTransformer extends AbstractTransformer<Long, Role, RoleDto> {

    private final PermissionTransformer permissionTransformer;

    public RoleTransformer(PermissionTransformer permissionTransformer) {
        super();
        this.permissionTransformer = permissionTransformer;
    }

    @Override
    protected void copyEntityToDto(Role entity, RoleDto dto) {
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPermissions(permissionTransformer.toDtoList(entity.getPermissions()));
    }

    @Override
    protected void copyDtoToEntity(RoleDto dto, Role entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPermissions(permissionTransformer.toEntityList(dto.getPermissions()));
    }
}
