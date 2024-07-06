package com.tutorial.springboot.rest_basic.service;

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
import static com.tutorial.springboot.rest_basic.validation.CollectionValidation.requireNotEmpty;
import static com.tutorial.springboot.rest_basic.validation.NumberValidation.requireEquality;
import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.requireNotNull;
import static java.util.Objects.nonNull;

@Service
public class InMemorySampleServiceImpl implements SampleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SampleRepository<Long, SampleEntity> sampleRepository;

    public InMemorySampleServiceImpl(SampleRepository<Long, SampleEntity> sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Optional<Long> insert(SampleDto dto) {
        requireNotNull(dto, "Sample should not be null");

        return sampleRepository.insert(toEntity(dto));
    }

    @Override
    public Optional<SampleDto> selectById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");

        return sampleRepository.selectById(id).map(SampleTransformer::toDto);
    }

    @Override
    public void update(Long id, SampleDto dto) {
        requireNotNull(id, "ID of Sample should not be null");
        requireNotNull(dto, "Sample should not be null");
        if (nonNull(dto.id())) {
            requireEquality(id, dto.id(), "There are two different values for Sample ID");
        }

        var exists = sampleRepository.exists(id);
        if (exists) {
            sampleRepository.update(toEntity(dto), id, dto.version());
        } else {
            logger.warn("Sample Entity with id {} not found", id);
        }
    }

    @Override
    public void deleteById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");
        sampleRepository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return sampleRepository.exists(id);
    }

    @Override
    public List<Long> insertBatch(SampleDto... dtos) {
        requireNotNull(dtos, "List of Sample should not be null");
        requireNotEmpty(dtos, "List of Sample should not be empty");

        return Stream.of(dtos)
                .map(this::insert)
                .map(Optional::orElseThrow)
                .toList();
    }

    @Override
    public List<SampleDto> selectBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        return sampleRepository.selectBatch(identities)
                .map(SampleTransformer::toDto)
                .toList();
    }

    @Override
    public void deleteBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        sampleRepository.deleteBatch(identities);
    }

    @Override
    public List<SampleDto> selectAll() {
        return sampleRepository.selectAll()
                .map(SampleTransformer::toDto)
                .toList();
    }

    @Override
    public void deleteAll() {
        sampleRepository.deleteAll();
    }

    @Override
    public List<Long> selectIdentities() {
        return sampleRepository.selectIdentities().toList();
    }

}
