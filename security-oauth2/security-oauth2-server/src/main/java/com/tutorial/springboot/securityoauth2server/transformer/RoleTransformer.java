package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleTransformer extends CodeTableTransformer<Long, Role, RoleDto> {

    private final PermissionTransformer permissionTransformer;

    public RoleTransformer(PermissionTransformer permissionTransformer) {
        super();
        this.permissionTransformer = permissionTransformer;
    }

    @Override
    protected void copyEntityToDto(Role entity, RoleDto dto) {
        super.copyEntityToDto(entity, dto);
        dto.setPermissions(permissionTransformer.toDtoList(entity.getPermissions()));
    }

    @Override
    protected void copyDtoToEntity(RoleDto dto, Role entity) {
        super.copyDtoToEntity(dto, entity);
        entity.setPermissions(permissionTransformer.toEntityList(dto.getPermissions()));
    }
}
