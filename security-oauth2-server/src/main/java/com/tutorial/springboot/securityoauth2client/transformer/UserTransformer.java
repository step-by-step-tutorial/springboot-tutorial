package com.tutorial.springboot.securityoauth2client.transformer;

import com.tutorial.springboot.securityoauth2client.dto.UserDto;
import com.tutorial.springboot.securityoauth2client.entity.User;
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
