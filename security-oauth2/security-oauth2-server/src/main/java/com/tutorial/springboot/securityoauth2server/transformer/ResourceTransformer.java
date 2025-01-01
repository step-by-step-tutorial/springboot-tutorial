package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.ResourceDto;
import com.tutorial.springboot.securityoauth2server.entity.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceTransformer extends AbstractTransformer<Long, Resource, ResourceDto> {

    private final ScopeTransformer scopeTransformer;

    public ResourceTransformer(ScopeTransformer scopeTransformer) {
        super();
        this.scopeTransformer = scopeTransformer;
    }

    @Override
    protected void copyEntityToDto(Resource entity, ResourceDto dto) {
        dto
        .setName(entity.getName())
        .setDescription(entity.getDescription())
        .setScopes(scopeTransformer.toStringList(entity.getScopes()));
    }

    @Override
    protected void copyDtoToEntity(ResourceDto dto, Resource entity) {
        entity
        .setName(dto.getName())
        .setDescription(dto.getDescription())
        .setScopes(scopeTransformer.fromStringList(dto.getScopes()));
    }
}
