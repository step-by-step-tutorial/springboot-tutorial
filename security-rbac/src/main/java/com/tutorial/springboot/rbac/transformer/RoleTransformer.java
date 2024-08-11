package com.tutorial.springboot.rbac.transformer;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.RolePermission;
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
        dto.setAuthority(entity.getAuthority());
        dto.setPermissions(
                entity.getRolePermissions()
                        .stream()
                        .map(rolePermission -> permissionTransformer.toDto(rolePermission.getPermission()))
                        .toList()
        );
    }

    @Override
    protected void copyDtoToEntity(RoleDto dto, Role entity) {
        entity.setAuthority(dto.getAuthority());
    }
}
