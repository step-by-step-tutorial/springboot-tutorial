package com.tutorial.springboot.restful_web_api.service;

import com.tutorial.springboot.restful_web_api.dto.Page;
import com.tutorial.springboot.restful_web_api.dto.SampleDto;
import com.tutorial.springboot.restful_web_api.entity.SampleEntity;
import com.tutorial.springboot.restful_web_api.repository.SampleRepository;
import com.tutorial.springboot.restful_web_api.transformer.SampleTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.tutorial.springboot.restful_web_api.transformer.SampleTransformer.toEntities;
import static com.tutorial.springboot.restful_web_api.transformer.SampleTransformer.toEntity;
import static com.tutorial.springboot.restful_web_api.validation.ObjectValidation.*;
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

        return repository.insert(toEntity(dto));
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
            logger.warn("{} Entity with id {} not found", SampleDto.class.getSimpleName(), id);
            throw new NoSuchElementException(String.format("%s Entity with id %s not found", SampleDto.class.getSimpleName(), id));
        }
    }

    @Override
    public void deleteById(Long id) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleDto.class.getSimpleName()));
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleDto.class.getSimpleName()));
        return repository.exists(id);
    }

    @Override
    public List<Long> saveBatch(SampleDto[] items) {
        shouldNotBeNull(items, String.format("List of %s should not be null", SampleDto.class.getSimpleName()));
        shouldNotBeNullOrEmpty(items, String.format("List of %s should not be empty", SampleDto.class.getSimpleName()));

        return repository.insertBatch(toEntities(items));
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
    public Optional<Page<SampleDto>> findBatch(int page, int size) {
        var items = repository.selectByPage(page, size)
                .map(SampleTransformer::toDto)
                .toList();
        var totalItems = repository.count();
        var totalPages = (int) Math.ceil((double) totalItems / size);

        return Optional.of(new Page<>(items, page, totalItems, totalPages));
    }

    @Override
    public void deleteBatch(Long[] identifiers) {
        shouldNotBeNull(identifiers, String.format("List of ID of %s should not be null", SampleDto.class.getSimpleName()));
        shouldNotBeNullOrEmpty(identifiers, String.format("List of ID of %s should not be empty", SampleDto.class.getSimpleName()));

        repository.deleteBatch(identifiers);
    }

    @Override
    public List<SampleDto> findAll() {
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
