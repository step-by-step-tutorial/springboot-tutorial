package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.ScopeDto;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.stereotype.Component;

@Component
public class ScopeTransformer extends AbstractTransformer<Long, Scope, ScopeDto> {

    @Override
    protected void copyEntityToDto(Scope entity, ScopeDto dto) {
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Override
    protected void copyDtoToEntity(ScopeDto dto, Scope entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
