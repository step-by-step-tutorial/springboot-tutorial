package com.tutorial.springboot.security_rbac_jwt.transformer;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PermissionTransformer extends AbstractTransformer<Long, Permission, PermissionDto> {

    @Override
    protected void copyEntityToDto(Permission entity, PermissionDto dto) {
        dto.setName(entity.getName());
    }

    @Override
    protected void copyDtoToEntity(PermissionDto dto, Permission entity) {
        entity.setName(dto.getName());
    }
}
