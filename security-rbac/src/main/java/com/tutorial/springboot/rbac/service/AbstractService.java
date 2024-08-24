package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.AbstractDto;
import com.tutorial.springboot.rbac.entity.AbstractEntity;
import com.tutorial.springboot.rbac.transformer.AbstractTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.tutorial.springboot.rbac.util.CollectionUtils.calculateNumberOfBatch;
import static com.tutorial.springboot.rbac.util.CollectionUtils.selectBatch;
import static com.tutorial.springboot.rbac.validation.CollectionValidation.requireNotEmpty;
import static com.tutorial.springboot.rbac.validation.ObjectValidation.requireNotNull;
import static java.util.Objects.requireNonNull;

public abstract class AbstractService<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> implements CrudService<ID, DTO>, BatchService<ID, DTO> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractService.class);

    protected final JpaRepository<ENTITY, ID> repository;

    protected final AbstractTransformer<ID, ENTITY, DTO> transformer;

    public AbstractService(JpaRepository<ENTITY, ID> repository, AbstractTransformer<ID, ENTITY, DTO> transformer) {
        this.repository = repository;
        this.transformer = transformer;
    }

    @Override
    public Optional<ID> save(DTO dto) {
        requireNonNull(dto);

        dto.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .setCreatedAt(LocalDateTime.now())
                .setVersion(1);

        var entity = repository.save(transformer.toEntity(dto));
        logger.info("Entity saved with ID: {}", entity.getId());
        return Optional.of(entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DTO> getById(ID id) {
        requireNonNull(id);

        return repository.findById(id).map(transformer::toDto);
    }

    @Override
    @Transactional
    public void update(ID id, DTO dto) {
        requireNonNull(id);
        requireNonNull(dto);

        var entityHolder = repository.findById(id);
        if (entityHolder.isPresent()) {
            ENTITY entity = entityHolder.get();
            entity.updateFrom(transformer.toEntity(dto));
            repository.save(entity);
        } else {
            logger.warn("Entity not found with ID: {}", id);
        }
    }

    @Override
    public void delete(ID id) {
        requireNonNull(id);

        repository.deleteById(id);
        logger.info("Entity deleted with ID: {}", id);
    }

    @Override
    public boolean exists(ID id) {
        return repository.existsById(id);
    }

    @Override
    public List<ID> saveBatch(List<DTO> dtoList) {
        requireNotNull(dtoList, "List of DTOs should not be null");
        requireNotEmpty(dtoList, "List of DTOs should not be empty");

        int numberOfBatches = calculateNumberOfBatch(dtoList.size());

        return IntStream.range(0, numberOfBatches)
                .mapToObj(i -> selectBatch(dtoList, i))
                .map(stream -> stream.map(transformer::toEntity))
                .map(batch -> repository.saveAll(batch.toList()))
                .flatMap(List::stream)
                .map(AbstractEntity::getId)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DTO> getAll() {
        return repository.findAll().stream().map(transformer::toDto).toList();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteBatch(List<ID> identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of IDs should not be empty");

        repository.deleteAllByIdInBatch(identities);
    }

}