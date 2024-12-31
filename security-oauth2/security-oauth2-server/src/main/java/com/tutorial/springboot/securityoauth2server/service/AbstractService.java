package com.tutorial.springboot.securityoauth2server.service;

import com.tutorial.springboot.securityoauth2server.dto.AbstractDto;
import com.tutorial.springboot.securityoauth2server.entity.AbstractEntity;
import com.tutorial.springboot.securityoauth2server.transformer.AbstractTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.tutorial.springboot.securityoauth2server.util.CollectionUtils.*;
import static com.tutorial.springboot.securityoauth2server.util.ReflectionUtils.identifyType;
import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentUsername;
import static com.tutorial.springboot.securityoauth2server.validation.ObjectValidation.shouldBeNotNullOrEmpty;
import static java.util.Objects.requireNonNull;

@Transactional
public abstract class AbstractService<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>>
        implements CrudService<ID, DTO>, BatchService<ID, DTO>, AllService<ID, DTO> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractService.class);

    protected final JpaRepository<ENTITY, ID> repository;

    protected final AbstractTransformer<ID, ENTITY, DTO> transformer;

    protected final Class<ENTITY> entityClass;

    protected final Class<DTO> dtoClass;

    public AbstractService(JpaRepository<ENTITY, ID> repository, AbstractTransformer<ID, ENTITY, DTO> transformer) {
        this.repository = repository;
        this.transformer = transformer;
        this.entityClass = identifyType(1, getClass());
        this.dtoClass = identifyType(2, getClass());
    }

    @Override
    @PreAuthorize("hasPermission(#dto, 'CREAT')")
    public Optional<ID> save(DTO dto) {
        requireNonNull(dto, String.format("%s should not be null", dtoClass.getSimpleName()));

        var newEntity = transformer.toEntity(dto);
        beforeSave(dto, newEntity);
        ENTITY savedEntity;
        try {
            savedEntity = repository.save(newEntity);
        } catch (Exception e) {
            logger.error("Failed to save {} entity due to {}", entityClass.getSimpleName(), e.getMessage());
            return Optional.empty();
        }
        afterSave(dto, savedEntity);
        logger.info("{} entity saved with ID: {}", entityClass.getSimpleName(), savedEntity.getId());
        return Optional.of(savedEntity.getId());
    }

    protected void beforeSave(DTO dto, ENTITY entity) {

    }

    protected void afterSave(DTO dto, ENTITY entity) {

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#dto, 'READ')")
    public Optional<DTO> findById(ID id) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));

        return repository.findById(id).map(transformer::toDto);
    }

    @Override
    @PreAuthorize("hasPermission(#dto, 'UPDATE')")
    public void update(ID id, DTO dto) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));
        requireNonNull(dto, String.format("%s should not be null", dtoClass.getSimpleName()));

        var entityOptional = repository.findById(id);
        if (entityOptional.isPresent()) {
            var entity = entityOptional.get();
            entity.updateFrom(transformer.toEntity(dto));
            repository.save(entity);
            logger.info("{} entity updated with ID: {}", entityClass.getSimpleName(), entity.getId());
        } else {
            logger.warn("{} entity not found with ID: {}", entityClass.getSimpleName(), id);
        }

    }

    @Override
    @PreAuthorize("hasPermission(#dto, 'DELETE')")
    public void deleteById(ID id) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));

        repository.deleteById(id);
        logger.info("{} entity deleted with ID: {}", entityClass.getSimpleName(), id);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#dto, 'READ')")
    public boolean exists(ID id) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));
        return repository.existsById(id);
    }

    @Override
    @PreAuthorize("hasPermission(#dto, 'CREAT')")
    public List<ID> saveBatch(List<DTO> dtoList) {
        requireNonNull(dtoList, String.format("List of %s should not be null", entityClass.getSimpleName()));
        shouldBeNotNullOrEmpty(dtoList, String.format("List of %s should not be empty", entityClass.getSimpleName()));

        return IntStream.range(0, calculateBatchNumber(dtoList.size()))
                .mapToObj(i -> selectBatch(dtoList, i, BATCH_SIZE))
                .flatMap(stream -> stream
                        .map(this::save)
                        .filter(Optional::isPresent)
                        .map(Optional::get))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#dto, 'READ')")
    public Page<DTO> findByPage(Pageable pageable) {
        requireNonNull(pageable, String.format("Page of %s should not be null", entityClass.getSimpleName()));
        return repository.findAll(pageable).map(transformer::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#dto, 'READ')")
    public List<DTO> findByIdentities(List<ID> identities) {
        requireNonNull(identities, String.format("List of ID of %s should not be null", entityClass.getSimpleName()));
        shouldBeNotNullOrEmpty(identities, String.format("List of ID of %s should not be empty", entityClass.getSimpleName()));

        return repository.findAllById(identities)
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasPermission(#dto, 'DELETE')")
    public void deleteBatch(List<ID> identities) {
        requireNonNull(identities, String.format("List of ID of %s should not be null", entityClass.getSimpleName()));
        shouldBeNotNullOrEmpty(identities, String.format("List of ID of %s should not be empty", entityClass.getSimpleName()));

        repository.deleteAllByIdInBatch(identities);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#dto, 'READ')")
    public List<DTO> findAll() {
        return repository.findAll()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasPermission(#dto, 'DELETE')")
    public void deleteAll() {
        logger.info("Delete all {} entities", entityClass.getSimpleName());
        repository.deleteAll();
    }
}