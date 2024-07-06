package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.validation.CollectionValidation.requireNotEmpty;
import static com.tutorial.springboot.rest_basic.validation.NumberValidation.requireEquality;
import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.requireNotNull;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

@Repository
public class InMemorySampleRepositoryImpl implements SampleRepository {

    @Override
    public Optional<Long> insert(SampleDto sample) {
        requireNotNull(sample, "Sample should not be null");

        final var id = InMemoryDatabase.SAMPLE_ID_GENERATOR.incrementAndGet();
        InMemoryDatabase.SAMPLE_TABLE.putIfAbsent(id, SampleDto.builder().from(sample).id(id).build());
        return Optional.of(id);
    }

    @Override
    public Optional<SampleDto> selectById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");

        final var sample = InMemoryDatabase.SAMPLE_TABLE.get(id);
        return Optional.ofNullable(sample);
    }

    @Override
    public void update(Long id, SampleDto dto) {
        requireNotNull(id, "ID of Sample should not be null");
        requireNotNull(dto, "Sample should not be null");
        if (nonNull(dto.id())) {
            requireEquality(id, dto.id(), "There are two different values for Sample ID");
        }

        InMemoryDatabase.SAMPLE_TABLE.put(id, SampleDto.builder().from(dto).id(id).build());
    }

    @Override
    public void deleteById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");
        InMemoryDatabase.SAMPLE_TABLE.remove(id);
    }

    @Override
    public boolean exists(Long id) {
        return InMemoryDatabase.SAMPLE_TABLE.containsKey(id);
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

        final var validIdentifiers = asList(identities);
        return InMemoryDatabase.SAMPLE_TABLE.values()
                .stream()
                .filter(sample -> validIdentifiers.contains(sample.id()))
                .toList();
    }

    @Override
    public void deleteBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        Stream.of(identities)
                .filter(Objects::nonNull)
                .forEach(InMemoryDatabase.SAMPLE_TABLE::remove);
    }

    @Override
    public List<SampleDto> selectAll() {
        return InMemoryDatabase.SAMPLE_TABLE.values()
                .stream()
                .toList();
    }

    @Override
    public void deleteAll() {
        InMemoryDatabase.SAMPLE_TABLE.clear();
    }

    @Override
    public List<Long> selectIdentities() {
        return InMemoryDatabase.SAMPLE_TABLE.keySet().stream().toList();
    }


}
