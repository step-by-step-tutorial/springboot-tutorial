package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;
import com.tutorial.springboot.security_rbac_jwt.entity.AbstractEntity;
import com.tutorial.springboot.security_rbac_jwt.transformer.AbstractTransformer;
import com.tutorial.springboot.security_rbac_jwt.validation.ObjectValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    public static final int INIT_VERSION = 1;

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
    public Optional<ID> save(DTO dto) {
        requireNonNull(dto, String.format("%s should not be null", dtoClass.getSimpleName()));

        dto.setCreatedBy(getCurrentUsername())
                .setCreatedAt(LocalDateTime.now())
                .setVersion(INIT_VERSION);

        var entity = repository.save(transformer.toEntity(dto));
        logger.info("{} entity saved with ID: {}", entityClass.getSimpleName(), entity.getId());
        return Optional.of(entity.getId());
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
        ObjectValidation.shouldBeNotNullOrEmpty(dtoList, String.format("List of %s should not be empty", entityClass.getSimpleName()));

        int numberOfBatches = calculateBatchNumber(dtoList.size());

        return IntStream.range(0, numberOfBatches)
                .mapToObj(i -> selectBatch(dtoList, i))
                .map(stream -> stream.map(transformer::toEntity))
                .map(batch -> repository.saveAll(batch.toList()))
                .flatMap(List::stream)
                .map(AbstractEntity::getId)
                .toList();
    }

    @Override
    public Page<DTO> getBatch(Pageable pageable) {
        requireNonNull(pageable, String.format("Page of %s should not be null", entityClass.getSimpleName()));
        return repository.findAll(pageable).map(transformer::toDto);
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

    @Override
    public void deleteBatch(List<ID> identities) {
        requireNonNull(identities, String.format("List of ID of %s should not be null", entityClass.getSimpleName()));
        ObjectValidation.shouldBeNotNullOrEmpty(identities, String.format("List of ID of %s should not be empty", entityClass.getSimpleName()));

        repository.deleteAllByIdInBatch(identities);
    }

}