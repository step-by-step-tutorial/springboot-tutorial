package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.entity.SampleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.repository.InMemoryDatabase.SAMPLE_TABLE;
import static com.tutorial.springboot.rest_basic.validation.CollectionValidation.requireNotEmpty;
import static com.tutorial.springboot.rest_basic.validation.NumberValidation.requireEquality;
import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.requireNotNull;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

@Repository
public class InMemorySampleRepositoryImpl implements SampleRepository<Long, SampleEntity> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Optional<Long> insert(SampleEntity entity) {
        requireNotNull(entity, "Sample should not be null");

        final var id = InMemoryDatabase.SAMPLE_ID_GENERATOR.incrementAndGet();
        entity.id(id);
        SAMPLE_TABLE.putIfAbsent(id, entity);
        return Optional.of(id);
    }

    @Override
    public Optional<SampleEntity> selectById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");

        final var sample = SAMPLE_TABLE.get(id);
        return Optional.ofNullable(sample);
    }

    @Override
    public void update(SampleEntity entity, Long id, Integer version) {
        requireNotNull(id, "ID of Sample should not be null");
        requireNotNull(entity, "Sample should not be null");
        if (nonNull(entity.id())) {
            requireEquality(id, entity.id(), "There are two different values for Sample ID");
        }
        var persisted = selectById(id);
        if (persisted.isPresent()) {
            var value = persisted.get();
            if (!Objects.equals(value.version(), version)) {
                throw new IllegalStateException(String.format("Sample entity [id %s and version %s] do not match with DTO [id %s and version %s] ", id, value.version(), id, version));
            }
            value.updateFrom(entity);
            SAMPLE_TABLE.put(id, value);
        } else {
            logger.warn("Sample Entity with id {} not found", id);
        }
    }

    @Override
    public void deleteById(Long id) {
        requireNotNull(id, "ID of Sample should not be null");
        SAMPLE_TABLE.remove(id);
    }

    @Override
    public boolean exists(Long id) {
        return SAMPLE_TABLE.containsKey(id);
    }

    @Override
    public Stream<Long> insertBatch(SampleEntity... entities) {
        requireNotNull(entities, "List of Sample should not be null");
        requireNotEmpty(entities, "List of Sample should not be empty");

        var identities = new ArrayList<Long>(entities.length);
        for (SampleEntity entity : entities) {
            var id = insert(entity);
            if (id.isPresent()) {
                identities.add(id.get());
            } else {
                throw new RuntimeException("Failed to insert entity: " + entity);
            }
        }
        return identities.stream();
    }

    @Override
    public Stream<SampleEntity> selectBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        final var validIdentifiers = asList(identities);
        return SAMPLE_TABLE.values()
                .stream()
                .filter(sample -> validIdentifiers.contains(sample.id()));
    }

    @Override
    public void deleteBatch(Long... identities) {
        requireNotNull(identities, "List of identities should not be null");
        requireNotEmpty(identities, "List of identifier should not be empty");

        Stream.of(identities)
                .filter(Objects::nonNull)
                .forEach(SAMPLE_TABLE::remove);
    }

    @Override
    public Stream<SampleEntity> selectAll() {
        return SAMPLE_TABLE.values().stream();
    }

    @Override
    public void deleteAll() {
        SAMPLE_TABLE.clear();
    }

    @Override
    public Stream<Long> selectIdentities() {
        return SAMPLE_TABLE.keySet().stream();
    }


}
