package com.tutorial.springboot.abac.transformer;

import com.tutorial.springboot.abac.dto.AbstractDto;
import com.tutorial.springboot.abac.entity.AbstractEntity;

import static com.tutorial.springboot.abac.util.ReflectionUtils.identifyType;
import static java.util.Objects.nonNull;

public abstract class AbstractTransformer<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    private Class<ENTITY> entityClass;

    private Class<DTO> dtoClass;

    public AbstractTransformer() {
        entityClass = identifyType(1, getClass());
        dtoClass = identifyType(2, getClass());
    }


    public DTO toDto(ENTITY entity) {
        DTO dto = createDto();

        if (nonNull(entity) || nonNull(dto)) {
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

        if (nonNull(entity) || nonNull(dto)) {
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