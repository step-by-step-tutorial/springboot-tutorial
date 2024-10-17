package com.tutorial.springboot.securityoauth2client.transformer;

import com.tutorial.springboot.securityoauth2client.dto.ScopeDto;
import com.tutorial.springboot.securityoauth2client.entity.Scope;
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
