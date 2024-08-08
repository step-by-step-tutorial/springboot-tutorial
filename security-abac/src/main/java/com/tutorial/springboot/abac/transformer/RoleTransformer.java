package com.tutorial.springboot.abac.transformer;

import com.tutorial.springboot.abac.dto.RoleDto;
import com.tutorial.springboot.abac.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleTransformer extends AbstractTransformer<Long, Role, RoleDto> {

    private final PermissionTransformer permissionTransformer;

    public RoleTransformer(PermissionTransformer permissionTransformer) {
        this.permissionTransformer = permissionTransformer;
    }

    @Override
    protected void copyEntityToDto(Role entity, RoleDto dto) {
        dto.setAuthority(entity.getAuthority());
        dto.setPermissions(entity.getPermissions().stream().map(permissionTransformer::toDto).toList());
    }

    @Override
    protected void copyDtoToEntity(RoleDto dto, Role entity) {
        entity.setAuthority(dto.getAuthority());
        entity.setPermissions(dto.getPermissions().stream().map(permissionTransformer::toEntity).toList());
    }
}
