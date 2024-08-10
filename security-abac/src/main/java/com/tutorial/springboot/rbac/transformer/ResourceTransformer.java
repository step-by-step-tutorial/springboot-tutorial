package com.tutorial.springboot.rbac.transformer;

import com.tutorial.springboot.rbac.dto.ResourceDto;
import com.tutorial.springboot.rbac.entity.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceTransformer extends AbstractTransformer<Long, Resource, ResourceDto> {

    @Override
    protected void copyEntityToDto(Resource entity, ResourceDto dto) {
        dto.setResourceName(entity.getResourceName());
        dto.setAttributes(entity.getAttributes());
    }

    @Override
    protected void copyDtoToEntity(ResourceDto dto, Resource entity) {
        entity.setResourceName(dto.getResourceName());
        entity.setAttributes(dto.getAttributes());
    }
}
