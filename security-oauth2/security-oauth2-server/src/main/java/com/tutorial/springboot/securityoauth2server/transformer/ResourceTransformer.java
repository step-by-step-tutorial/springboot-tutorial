package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.ResourceDto;
import com.tutorial.springboot.securityoauth2server.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
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
        dto.setResourceId(entity.getResourceId())
        .setResourceName(entity.getResourceName())
        .setResourceDescription(entity.getResourceDescription())
        .setScopes(scopeTransformer.toScopeStringList(entity.getScopes()));
    }

    @Override
    protected void copyDtoToEntity(ResourceDto dto, Resource entity) {
        entity.setResourceId(dto.getResourceId())
        .setResourceName(dto.getResourceName())
        .setResourceDescription(dto.getResourceDescription())
        .setScopes(scopeTransformer.toScopeEntityList(dto.getScopes()));
    }
}
