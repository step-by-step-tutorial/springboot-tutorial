package com.tutorial.springboot.rest_basic.service;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.repository.SampleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.validation.CollectionValidation.requireNotEmpty;
import static com.tutorial.springboot.rest_basic.validation.NumberValidation.requireEquality;
import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.requireNotNull;
import static java.util.Objects.nonNull;

@Service
public class InMemorySampleServiceImpl implements SampleService {

    final SampleRepository sampleRepository;

    public InMemorySampleServiceImpl(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Optional<Long> insert(SampleDto sample) {
        requireNotNull(sample, "Sample should not be null");

        return Optional.of(sampleRepository.insert(sample).orElseThrow());
    }

    @Override
    public Optional<SampleDto> selectById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");

        return Optional.of(sampleRepository.selectById(id).orElseThrow());
    }

    @Override
    public void update(Long id, SampleDto dto) {
        requireNotNull(id, "ID of Sample should not be null");
        requireNotNull(dto, "Sample should not be null");
        if (nonNull(dto.id())) {
            requireEquality(id, dto.id(), "There are two different values for Sample ID");
        }

        sampleRepository.update(id, dto);
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
    public List<Long> insertBatch(SampleDto... samples) {
        requireNotNull(samples, "List of Sample should not be null");
        requireNotEmpty(samples, "List of Sample should not be empty");

        return Stream.of(samples)
                .map(this::insert)
                .map(Optional::orElseThrow)
                .toList();
    }

    @Override
    public List<SampleDto> selectBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        return sampleRepository.selectBatch(identities);
    }

    @Override
    public void deleteBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        sampleRepository.deleteBatch(identities);
    }

    @Override
    public List<SampleDto> selectAll() {
        return sampleRepository.selectAll();
    }

    @Override
    public void deleteAll() {
        sampleRepository.deleteAll();
    }

    @Override
    public List<Long> selectIdentities() {
        return sampleRepository.selectIdentities();
    }

}
