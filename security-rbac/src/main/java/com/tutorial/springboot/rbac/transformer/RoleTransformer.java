package com.tutorial.springboot.rbac.transformer;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleTransformer extends AbstractTransformer<Long, Role, RoleDto> {

    @Override
    protected void copyEntityToDto(Role entity, RoleDto dto) {
        dto.setAuthority(entity.getAuthority());
    }

    @Override
    protected void copyDtoToEntity(RoleDto dto, Role entity) {
        entity.setAuthority(dto.getAuthority());
    }
}
