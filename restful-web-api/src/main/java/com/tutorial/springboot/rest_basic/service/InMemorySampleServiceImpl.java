package com.tutorial.springboot.rest_basic.service;

import com.tutorial.springboot.rest_basic.dto.Page;
import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.entity.SampleEntity;
import com.tutorial.springboot.rest_basic.repository.SampleRepository;
import com.tutorial.springboot.rest_basic.transformer.SampleTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.transformer.SampleTransformer.toEntity;
import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.*;
import static java.util.Objects.nonNull;

@Service
public class InMemorySampleServiceImpl implements SampleService<Long, SampleDto> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SampleRepository<Long, SampleEntity> repository;

    public InMemorySampleServiceImpl(SampleRepository<Long, SampleEntity> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Long> save(SampleDto dto) {
        shouldNotBeNull(dto, String.format("%s should not be null", SampleDto.class.getSimpleName()));

        return repository.insert(toEntity(dto).withInitialVersion());
    }

    @Override
    public Optional<SampleDto> findById(Long id) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleDto.class.getSimpleName()));

        return repository.selectById(id).map(SampleTransformer::toDto);
    }

    @Override
    public void update(Long id, SampleDto dto) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleDto.class.getSimpleName()));
        shouldNotBeNull(dto, String.format("%s should not be null", SampleDto.class.getSimpleName()));
        if (nonNull(dto.id())) {
            shouldBeEqual(id, dto.id(), String.format("There are two different values for %s ID", SampleDto.class.getSimpleName()));
        }

        var exists = repository.exists(id);
        if (exists) {
            repository.update(id, toEntity(dto));
        } else {
            logger.warn("Sample Entity with id {} not found", id);
        }
    }

    @Override
    public void deleteById(Long id) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleDto.class.getSimpleName()));
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.exists(id);
    }

    @Override
    public List<Long> batchSave(SampleDto[] items) {
        shouldNotBeNull(items, String.format("List of %s should not be null", SampleDto.class.getSimpleName()));
        shouldNotBeNullOrEmpty(items, String.format("List of %s should not be empty", SampleDto.class.getSimpleName()));

        return Stream.of(items)
                .map(this::save)
                .map(Optional::orElseThrow)
                .toList();
    }

    @Override
    public List<SampleDto> findByIdentifiers(Long[] identifiers) {
        shouldNotBeNull(identifiers, String.format("List of ID of %s should not be null", SampleDto.class.getSimpleName()));
        shouldNotBeNullOrEmpty(identifiers, String.format("List of ID of %s should not be empty", SampleDto.class.getSimpleName()));

        return repository.selectBatch(identifiers)
                .map(SampleTransformer::toDto)
                .toList();
    }

    @Override
    public Optional<Page<SampleDto>> findByPage(int page, int size) {
        var items = repository.selectByPage(page, size)
                .map(SampleTransformer::toDto)
                .toList();
        var totalItems = repository.count();
        var totalPages = (int) Math.ceil((double) totalItems / size);

        return Optional.of(new Page<>(items, page, totalItems, totalPages));
    }

    @Override
    public void batchDelete(Long[] identifiers) {
        shouldNotBeNull(identifiers, String.format("List of ID of %s should not be null", SampleDto.class.getSimpleName()));
        shouldNotBeNullOrEmpty(identifiers, String.format("List of ID of %s should not be empty", SampleDto.class.getSimpleName()));

        repository.deleteBatch(identifiers);
    }

    @Override
    public List<SampleDto> selectAll() {
        return repository.selectAll()
                .map(SampleTransformer::toDto)
                .toList();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Long> getIdentifiers() {
        return repository.selectIdentifiers().toList();
    }

}
