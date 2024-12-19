package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;
import com.tutorial.springboot.security_rbac_jwt.entity.AbstractEntity;
import com.tutorial.springboot.security_rbac_jwt.transformer.AbstractTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.calculateBatchNumber;
import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.selectBatch;
import static com.tutorial.springboot.security_rbac_jwt.util.ReflectionUtils.identifyType;
import static com.tutorial.springboot.security_rbac_jwt.util.SecurityUtils.getCurrentUsername;
import static com.tutorial.springboot.security_rbac_jwt.validation.ObjectValidation.shouldBeNotNullOrEmpty;
import static java.util.Objects.requireNonNull;

public abstract class AbstractService<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>>
        implements CrudService<ID, DTO>, BatchService<ID, DTO>, AllService<ID, DTO> {

    public static final int INIT_VERSION = 0;

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
    @Transactional
    public Optional<ID> save(DTO dto) {
        requireNonNull(dto, String.format("%s should not be null", dtoClass.getSimpleName()));

        dto.setCreatedBy(getCurrentUsername())
                .setCreatedAt(LocalDateTime.now())
                .setVersion(INIT_VERSION);

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

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    protected void beforeSave(DTO dto, ENTITY entity) {

    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    protected void afterSave(DTO dto, ENTITY entity) {

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DTO> getById(ID id) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));

        return repository.findById(id).map(transformer::toDto);
    }

    @Override
    @Transactional
    public void update(ID id, DTO dto) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));
        requireNonNull(dto, String.format("%s should not be null", dtoClass.getSimpleName()));

        repository.findById(id)
                .ifPresentOrElse(
                        entity -> {
                            entity.updateFrom(transformer.toEntity(dto));
                            repository.save(entity);
                            logger.info("{} entity updated with ID: {}", entityClass.getSimpleName(), entity.getId());
                        },
                        () -> logger.warn("{} entity not found with ID: {}", entityClass.getSimpleName(), id)
                );
    }

    @Override
    public void deleteById(ID id) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));

        repository.deleteById(id);
        logger.info("{} entity deleted with ID: {}", entityClass.getSimpleName(), id);
    }

    @Override
    public boolean exists(ID id) {
        requireNonNull(id, String.format("ID of %s should not be null", entityClass.getSimpleName()));
        return repository.existsById(id);
    }

    @Override
    public List<ID> saveBatch(List<DTO> dtoList) {
        requireNonNull(dtoList, String.format("List of %s should not be null", entityClass.getSimpleName()));
        shouldBeNotNullOrEmpty(dtoList, String.format("List of %s should not be empty", entityClass.getSimpleName()));

        return IntStream.range(0, calculateBatchNumber(dtoList.size()))
                .mapToObj(i -> selectBatch(dtoList, i))
                .flatMap(stream -> stream
                        .map(this::save)
                        .map(id -> id.orElse(null)))
                .toList();
    }

    @Override
    public Page<DTO> getBatch(Pageable pageable) {
        requireNonNull(pageable, String.format("Page of %s should not be null", entityClass.getSimpleName()));
        return repository.findAll(pageable).map(transformer::toDto);
    }

    @Override
    public List<DTO> getBatch(List<ID> identities) {
        requireNonNull(identities, String.format("List of ID of %s should not be null", entityClass.getSimpleName()));
        shouldBeNotNullOrEmpty(identities, String.format("List of ID of %s should not be empty", entityClass.getSimpleName()));

        return repository.findAllById(identities)
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @Override
    public void deleteBatch(List<ID> identities) {
        requireNonNull(identities, String.format("List of ID of %s should not be null", entityClass.getSimpleName()));
        shouldBeNotNullOrEmpty(identities, String.format("List of ID of %s should not be empty", entityClass.getSimpleName()));

        repository.deleteAllByIdInBatch(identities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DTO> getAll() {
        return repository.findAll()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @Override
    public void deleteAll() {
        logger.info("Delete all {} entities", entityClass.getSimpleName());
        repository.deleteAll();
    }

}