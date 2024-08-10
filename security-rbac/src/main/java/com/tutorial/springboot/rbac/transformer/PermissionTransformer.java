package com.tutorial.springboot.rbac.transformer;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.entity.Permission;
import org.springframework.stereotype.Component;

@Component
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
