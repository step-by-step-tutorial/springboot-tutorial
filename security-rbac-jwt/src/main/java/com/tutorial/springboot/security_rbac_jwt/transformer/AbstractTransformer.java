package com.tutorial.springboot.security_rbac_jwt.transformer;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;
import com.tutorial.springboot.security_rbac_jwt.entity.AbstractEntity;

import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.Stream;

import static com.tutorial.springboot.security_rbac_jwt.util.ReflectionUtils.identifyType;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public abstract class AbstractTransformer<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    private static final boolean ALLOWANCE_EXPOSE_SECURE_INFORMATION = true;

    private static final boolean DIS_ALLOWANCE_EXPOSE_SECURE_INFORMATION = false;

    private final Class<ENTITY> entityClass;

    private final Class<DTO> dtoClass;

    private boolean exposeSecureInformation = DIS_ALLOWANCE_EXPOSE_SECURE_INFORMATION;

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

        if (exposeSecureInformation == ALLOWANCE_EXPOSE_SECURE_INFORMATION) {
            exposeSecureInformation(entity, dto);
        }

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

    public List<DTO> toDtoList(List<ENTITY> entities) {
        return entities.stream().map(this::toDto).collect(toList());
    }

    public DTO[] toDtoArray(ENTITY[] entities) {
        return Stream.of(entities).map(this::toDto).toArray(size -> (DTO[]) Array.newInstance(dtoClass, size));
    }

    public List<ENTITY> toEntityList(List<DTO> entities) {
        return entities.stream().map(this::toEntity).collect(toList());
    }

    public ENTITY[] toEntityArray(DTO[] entities) {
        return Stream.of(entities).map(this::toEntity).toArray(size -> (ENTITY[]) Array.newInstance(entityClass, size));
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

    public void allowToExposeSecureInformation() {
        this.exposeSecureInformation = ALLOWANCE_EXPOSE_SECURE_INFORMATION;
    }

    public void disAllowToExposeSecureInformation() {
        this.exposeSecureInformation = DIS_ALLOWANCE_EXPOSE_SECURE_INFORMATION;
    }

    protected void exposeSecureInformation(ENTITY entity, DTO dto) {
    }

    protected abstract void copyEntityToDto(ENTITY entity, DTO dto);

    protected abstract void copyDtoToEntity(DTO dto, ENTITY entity);
}
