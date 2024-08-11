package com.tutorial.springboot.rbac.transformer;

import com.tutorial.springboot.rbac.dto.AbstractDto;
import com.tutorial.springboot.rbac.entity.AbstractEntity;

import java.util.List;

import static com.tutorial.springboot.rbac.util.ReflectionUtils.identifyType;
import static java.util.Objects.isNull;

public abstract class AbstractTransformer<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    private Class<ENTITY> entityClass;

    private Class<DTO> dtoClass;

    public AbstractTransformer() {
        entityClass = identifyType(1, getClass());
        dtoClass = identifyType(2, getClass());
    }


    public DTO toDto(ENTITY entity) {
        DTO dto = createDto();

        if (isNull(entity) || isNull(dto)) {
            return null;
        }

        dto.setId(entity.getId())
                .setCreatedBy(entity.getCreatedBy())
                .setCreatedAt(entity.getCreatedAt())
                .setUpdatedBy(entity.getUpdatedBy())
                .setUpdatedAt(entity.getUpdatedAt())
                .setVersion(entity.getVersion());

        copyEntityToDto(entity, dto);
        return dto;
    }

    public ENTITY toEntity(DTO dto) {
        ENTITY entity = createEntity();

        if (isNull(entity) || isNull(dto)) {
            return null;
        }

        entity.setId(dto.getId())
                .setCreatedBy(dto.getCreatedBy())
                .setCreatedAt(dto.getCreatedAt())
                .setUpdatedBy(dto.getUpdatedBy())
                .setUpdatedAt(dto.getUpdatedAt())
                .setVersion(dto.getVersion());

        copyDtoToEntity(dto, entity);
        return entity;
    }

    public List<ENTITY> toEntity(List<DTO> list) {
        return list.stream()
                .map(this::toEntity)
                .toList();
    }

    protected DTO createDto() {
        try {
            return dtoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    protected ENTITY createEntity() {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    protected abstract void copyEntityToDto(ENTITY entity, DTO dto);

    protected abstract void copyDtoToEntity(DTO dto, ENTITY entity);
}