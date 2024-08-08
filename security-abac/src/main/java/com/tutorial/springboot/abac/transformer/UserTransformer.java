package com.tutorial.springboot.abac.transformer;

import com.tutorial.springboot.abac.dto.UserDto;
import com.tutorial.springboot.abac.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer extends AbstractTransformer<Long, User, UserDto> {

    private final RoleTransformer roleTransformer;

    private UserTransformer(RoleTransformer roleTransformer) {
        this.roleTransformer = roleTransformer;
    }

    @Override
    protected void copyEntityToDto(User entity, UserDto dto) {
        dto.setUsername(entity.getUsername())
                .setPassword(entity.getPassword())
                .setEnabled(entity.isEnabled());

    }

    @Override
    protected void copyDtoToEntity(UserDto dto, User entity) {
        entity.setUsername(dto.getUsername())
                .setPassword(dto.getPassword())
                .setEnabled(dto.isEnabled())
                .setAuthorities(dto.getRoles().stream().map(roleTransformer::toEntity).toList());
    }

}
