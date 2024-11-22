package com.tutorial.springboot.restful_web_api.repository;

import com.tutorial.springboot.restful_web_api.entity.SampleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

import static com.tutorial.springboot.restful_web_api.repository.InMemoryDatabase.SAMPLE_ID_GENERATOR;
import static com.tutorial.springboot.restful_web_api.repository.InMemoryDatabase.SAMPLE_TABLE;
import static com.tutorial.springboot.restful_web_api.validation.ObjectValidation.*;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

@Repository
public class InMemorySampleRepositoryImpl implements SampleRepository<Long, SampleEntity> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Optional<Long> insert(SampleEntity entity) {
        shouldNotBeNull(entity, String.format("%s should not be null", SampleEntity.class.getSimpleName()));

        final var id = SAMPLE_ID_GENERATOR.incrementAndGet();
        SAMPLE_TABLE.putIfAbsent(id, entity.id(id).withInitialVersion());

        return Optional.of(id);
    }

    @Override
    public Optional<SampleEntity> selectById(Long id) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleEntity.class.getSimpleName()));

        final var sample = SAMPLE_TABLE.get(id);
        return Optional.ofNullable(sample);
    }

    @Override
    public void update(Long id, SampleEntity newOne) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleEntity.class.getSimpleName()));
        shouldNotBeNull(newOne, String.format("%s should not be null", SampleEntity.class.getSimpleName()));
        if (nonNull(newOne.id())) {
            shouldBeEqual(id, newOne.id(), String.format("There are two different values for %s ID", SampleEntity.class.getSimpleName()));
        }
        selectById(id).ifPresentOrElse(
                entity -> SAMPLE_TABLE.put(id, entity.updateFrom(newOne)),
                () -> logger.warn("{} Entity with id {} not found", SampleEntity.class.getSimpleName(), id)
        );
    }

    @Override
    public void deleteById(Long id) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleEntity.class.getSimpleName()));
        SAMPLE_TABLE.remove(id);
    }

    @Override
    public boolean exists(Long id) {
        shouldNotBeNull(id, String.format("ID of %s should not be null", SampleEntity.class.getSimpleName()));
        return SAMPLE_TABLE.containsKey(id);
    }

    @Override
    public List<Long> insertBatch(SampleEntity[] entities) {
        shouldNotBeNull(entities, String.format("List of %s should not be null", SampleEntity.class.getSimpleName()));
        shouldNotBeNullOrEmpty(entities, String.format("List of %s should not be empty", SampleEntity.class.getSimpleName()));

        return Arrays.stream(entities)
                .map(entity -> insert(entity).orElseThrow(() -> new RuntimeException(String.format("Failed to insert entity %s", entity))))
                .toList();
    }

    @Override
    public Stream<SampleEntity> selectBatch(Long[] identifiers) {
        shouldNotBeNull(identifiers, "List of ID should not be null");
        shouldNotBeNullOrEmpty(identifiers, "List of ID should not be empty");

        final var validIdentifiers = asList(identifiers);
        return SAMPLE_TABLE.values()
                .stream()
                .filter(sample -> validIdentifiers.contains(sample.id()));
    }

    @Override
    public void deleteBatch(Long[] identifiers) {
        shouldNotBeNull(identifiers, "List of ID should not be null");
        shouldNotBeNullOrEmpty(identifiers, "List of ID should not be empty");

        Stream.of(identifiers)
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
    public Stream<Long> selectIdentifiers() {
        return SAMPLE_TABLE.keySet().stream();
    }

    @Override
    public Stream<SampleEntity> selectByPage(int page, int size) {
        return SAMPLE_TABLE.values()
                .stream()
                .sorted(Comparator.comparing(SampleEntity::id))
                .skip((long) page * size)
                .limit(size);
    }

    @Override
    public int count() {
        return SAMPLE_TABLE.size();
    }
}
