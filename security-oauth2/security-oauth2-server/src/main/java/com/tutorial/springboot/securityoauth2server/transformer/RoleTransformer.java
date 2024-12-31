package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.transformer.AbstractTransformer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
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
