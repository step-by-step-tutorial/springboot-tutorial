package com.tutorial.springboot.rbac.transformer;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.entity.Role;
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
        dto.setPermissions(
                entity.getPermissions()
                        .stream()
                        .map(permissionTransformer::toDto)
                        .toList()
        );
    }

    @Override
    protected void copyDtoToEntity(RoleDto dto, Role entity) {
        entity.setName(dto.getName());
        entity.setPermissions(dto.getPermissions().stream().map(permissionTransformer::toEntity).toList());
    }
}
