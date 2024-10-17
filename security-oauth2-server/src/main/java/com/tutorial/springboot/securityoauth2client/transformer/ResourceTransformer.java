package com.tutorial.springboot.securityoauth2client.transformer;

import com.tutorial.springboot.securityoauth2client.dto.ResourceDto;
import com.tutorial.springboot.securityoauth2client.entity.Resource;
import com.tutorial.springboot.securityoauth2client.entity.Scope;
import org.springframework.stereotype.Component;

@Component
public class ResourceTransformer extends AbstractTransformer<Long, Resource, ResourceDto> {

    @Override
    protected void copyEntityToDto(Resource entity, ResourceDto dto) {
        dto.setResourceId(entity.getResourceId());
        dto.setResourceName(entity.getResourceName());
        dto.setResourceDescription(entity.getResourceDescription());
        dto.setScopes(entity.getScopes().stream().map(Scope::getName).toList());
    }

    @Override
    protected void copyDtoToEntity(ResourceDto dto, Resource entity) {
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
