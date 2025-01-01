package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.CodeTableDto;
import com.tutorial.springboot.securityoauth2server.entity.CodeTable;

import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class CodeTableTransformer<ID, ENTITY extends CodeTable<ID, ENTITY>, DTO extends CodeTableDto<ID, DTO>>
        extends AbstractTransformer<ID, ENTITY, DTO> {

    @Override
    protected void copyEntityToDto(ENTITY entity, DTO dto) {
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Override
    protected void copyDtoToEntity(DTO dto, ENTITY entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }

    public List<String> toStringList(List<ENTITY> grantTypes) {
        return grantTypes.stream()
                .map(ENTITY::getName)
                .collect(toList());
    }

    public List<ENTITY> fromStringList(List<String> grantTypes) {
        return grantTypes.stream()
                .map(it -> createEntity().setName(it))
                .collect(toList());
    }
}
