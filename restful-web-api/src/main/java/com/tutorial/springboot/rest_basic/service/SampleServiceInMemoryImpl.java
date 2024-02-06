package com.tutorial.springboot.rest_basic.service;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.repository.Database.SAMPLE_ID_GENERATOR;
import static com.tutorial.springboot.rest_basic.repository.Database.SAMPLE_TABLE;
import static com.tutorial.springboot.rest_basic.validation.NumberValidation.requireEquality;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Repository
public class SampleServiceInMemoryImpl implements SampleService {

    @Override
    public Optional<Long> insert(SampleDto sample) {
        requireNonNull(sample, "Sample should not be null");

        final var id = SAMPLE_ID_GENERATOR.incrementAndGet();
        SAMPLE_TABLE.putIfAbsent(id, SampleDto.builder().from(sample).id(id).build());
        return Optional.of(id);
    }

    @Override
    public Optional<SampleDto> selectById(Long id) {
        requireNonNull(id, "ID of Sample should not be null");

        final var sample = SAMPLE_TABLE.get(id);
        return Optional.ofNullable(sample);
    }

    @Override
    public void update(Long id, SampleDto dto) {
        requireNonNull(id, "ID of Sample should not be null");
        requireNonNull(dto, "Sample should not be null");
        if (!isNull(dto.id())) {
            requireEquality(id, dto.id(), "There are two different values for Sample ID");
        }

        SAMPLE_TABLE.put(id, SampleDto.builder().from(dto).id(id).build());
    }

    @Override
    public void deleteById(Long id) {
        requireNonNull(id, "ID of Sample should not be null");
        SAMPLE_TABLE.remove(id);
    }

    @Override
    public List<Long> insertAll(SampleDto... samples) {
        requireNonNull(samples, "List of Sample should not be null");
        if (samples.length == 0) {
            throw new IllegalStateException("List of Sample should not be empty");
        }

        return Stream.of(samples)
                .map(this::insert)
                .map(Optional::orElseThrow)
                .toList();
    }

    @Override
    public List<SampleDto> selectAll(Long... identities) {
        requireNonNull(identities, "List of identities should not be null");
        if (identities.length == 0) {
            throw new IllegalStateException("List of identifier should not be empty");
        }

        final var listOfIdentifiers = asList(identities);
        return SAMPLE_TABLE.values()
                .stream()
                .filter(sample -> listOfIdentifiers.contains(sample.id()))
                .toList();
    }

    @Override
    public List<SampleDto> selectAll() {
        return SAMPLE_TABLE.values()
                .stream()
                .toList();
    }

    @Override
    public void deleteAll(Long... identities) {
        requireNonNull(identities, "List of identities should not be null");
        if (identities.length == 0) {
            throw new IllegalStateException("List of identifier should not be empty");
        }

        Stream.of(identities).forEach(SAMPLE_TABLE::remove);
    }

    @Override
    public void truncate() {
        SAMPLE_TABLE.clear();
    }

    @Override
    public List<Long> getIdentities() {
        return SAMPLE_TABLE.keySet().stream().toList();
    }

    @Override
    public boolean exists(Long id) {
        return SAMPLE_TABLE.containsKey(id);
    }

}
