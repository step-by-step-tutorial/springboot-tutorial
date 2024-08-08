package com.tutorial.springboot.abac.transformer;

import com.tutorial.springboot.abac.dto.PermissionDto;
import com.tutorial.springboot.abac.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionTransformer extends AbstractTransformer<Long, Permission, PermissionDto> {

    private PermissionTransformer() {
    }

    @Override
    protected void copyEntityToDto(Permission entity, PermissionDto dto) {
        dto.setName(entity.getName());
    }

    @Override
    protected void copyDtoToEntity(PermissionDto dto, Permission entity) {
        entity.setName(dto.getName());
    }
}
