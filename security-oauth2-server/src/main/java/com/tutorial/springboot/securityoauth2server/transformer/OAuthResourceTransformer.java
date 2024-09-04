package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.OAuthResourceDto;
import com.tutorial.springboot.securityoauth2server.entity.OAuthResource;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.stereotype.Component;

@Component
public class OAuthResourceTransformer extends AbstractTransformer<Long, OAuthResource, OAuthResourceDto> {

    @Override
    protected void copyEntityToDto(OAuthResource entity, OAuthResourceDto dto) {
        dto.setResourceId(entity.getResourceId());
        dto.setResourceName(entity.getResourceName());
        dto.setResourceDescription(entity.getResourceDescription());
        dto.setScopes(entity.getScopes().stream().map(Scope::getName).toList());
    }

    @Override
    protected void copyDtoToEntity(OAuthResourceDto dto, OAuthResource entity) {
        entity.setResourceId(dto.getResourceId());
        entity.setResourceName(dto.getResourceName());
        entity.setResourceDescription(dto.getResourceDescription());
        entity.setScopes(dto.getScopes().stream().map(dto1 -> {
            Scope scope = new Scope();
            scope.setName(dto1);
            return scope;
        }).toList());
    }
}
