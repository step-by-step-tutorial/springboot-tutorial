package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.entity.User;
import com.tutorial.springboot.securityoauth2server.transformer.AbstractTransformer;
import com.tutorial.springboot.securityoauth2server.transformer.RoleTransformer;
import org.springframework.context.annotation.Scope;
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
                .setEmail(entity.getEmail())
                .setEnabled(entity.isEnabled())
                .setRoles(roleTransformer.toDtoList(entity.getRoles()));
    }

    @Override
    protected void copyDtoToEntity(UserDto dto, User entity) {
        entity.setUsername(dto.getUsername())
                .setPassword(dto.getPassword())
                .setEmail(dto.getEmail())
                .setEnabled(dto.isEnabled())
                .setRoles(roleTransformer.toEntityList(dto.getRoles()));
    }
}
