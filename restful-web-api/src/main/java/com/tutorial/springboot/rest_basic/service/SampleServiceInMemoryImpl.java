package com.tutorial.springboot.rest_basic.service;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.repository.SampleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.validation.CollectionValidation.requireNotEmpty;
import static com.tutorial.springboot.rest_basic.validation.NumberValidation.requireEquality;
import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.requireNotNull;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

@Repository
public class SampleServiceInMemoryImpl implements SampleService {

    @Override
    public Optional<Long> insert(SampleDto sample) {
        requireNotNull(sample, "Sample should not be null");

        final var id = SampleRepository.ID_GENERATOR.incrementAndGet();
        SampleRepository.OPERATIONS.putIfAbsent(id, SampleDto.builder().from(sample).id(id).build());
        return Optional.of(id);
    }

    @Override
    public Optional<SampleDto> selectById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");

        final var sample = SampleRepository.OPERATIONS.get(id);
        return Optional.ofNullable(sample);
    }

    @Override
    public void update(Long id, SampleDto dto) {
        requireNotNull(id, "ID of Sample should not be null");
        requireNotNull(dto, "Sample should not be null");
        if (nonNull(dto.id())) {
            requireEquality(id, dto.id(), "There are two different values for Sample ID");
        }

        SampleRepository.OPERATIONS.put(id, SampleDto.builder().from(dto).id(id).build());
    }

    @Override
    public void deleteById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");
        SampleRepository.OPERATIONS.remove(id);
    }

    @Override
    public boolean exists(Long id) {
        return SampleRepository.OPERATIONS.containsKey(id);
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

        final var listOfIdentifiers = asList(identities);
        return SampleRepository.OPERATIONS.values()
                .stream()
                .filter(sample -> listOfIdentifiers.contains(sample.id()))
                .toList();
    }

    @Override
    public void deleteBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        Stream.of(identities)
                .filter(Objects::nonNull)
                .forEach(SampleRepository.OPERATIONS::remove);
    }

    @Override
    public List<SampleDto> selectAll() {
        return SampleRepository.OPERATIONS.values()
                .stream()
                .toList();
    }

    @Override
    public void deleteAll() {
        SampleRepository.OPERATIONS.clear();
    }

    @Override
    public List<Long> selectIdentities() {
        return SampleRepository.OPERATIONS.keySet().stream().toList();
    }

}
