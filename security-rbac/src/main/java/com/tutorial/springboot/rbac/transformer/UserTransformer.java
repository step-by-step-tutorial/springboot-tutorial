package com.tutorial.springboot.rbac.transformer;

import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer extends AbstractTransformer<Long, User, UserDto> {

    private final RoleTransformer roleTransformer;

    public UserTransformer(RoleTransformer roleTransformer) {
        super();
        this.roleTransformer = roleTransformer;
    }

    @Override
    protected void copyEntityToDto(User entity, UserDto dto) {
        dto.setUsername(entity.getUsername())
                .setPassword(entity.getPassword())
                .setEmail(entity.getEmail())
                .setEnabled(entity.isEnabled())
                .setRoles(entity.getRoles().stream().map(roleTransformer::toDto).toList());
    }

    @Override
    protected void copyDtoToEntity(UserDto dto, User entity) {
        entity.setUsername(dto.getUsername())
                .setPassword(dto.getPassword())
                .setEmail(dto.getEmail())
                .setEnabled(dto.isEnabled())
                .setRoles(dto.getRoles().stream().map(roleTransformer::toEntity).toList());
    }

}
