package com.tutorial.springboot.cdcdebezium.repository;

import com.tutorial.springboot.cdcdebezium.model.ExampleDto;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tutorial.springboot.cdcdebezium.util.JsonUtils.toJson;
import static com.tutorial.springboot.cdcdebezium.util.JsonUtils.toModel;
import static java.util.Objects.requireNonNull;

@Repository
@Profile({"redis", "embedded-redis"})
public class CacheExampleRepository {

    private static final String Hash_KEY = "Example";

    private final Logger logger = LoggerFactory.getLogger(JdbcExampleRepository.class);

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    public Optional<String> save(ExampleDto model) {
        requireNonNull(model);

        try {
            model.setId(UUID.randomUUID().toString());
            hashOperations.putIfAbsent(Hash_KEY, model.getId(), toJson(model));
            return Optional.ofNullable(model.getId());
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return Optional.empty();
        }
    }

    public void update(ExampleDto model) {
        requireNonNull(model);
        requireNonNull(model.getId());

        try {
            hashOperations.put(Hash_KEY, model.getId(), toJson(model));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public Optional<ExampleDto> findById(String id) {
        requireNonNull(id);

        try {
            final var jsonString = hashOperations.get(Hash_KEY, id);
            return Optional.ofNullable(toModel(jsonString, ExampleDto.class));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return Optional.empty();
        }
    }

    public List<ExampleDto> findAll() {
        return hashOperations.entries(Hash_KEY)
                .values()
                .stream()
                .map(item -> {
                    try {
                        return toModel(item, ExampleDto.class);
                    } catch (Exception exception) {
                        logger.error(exception.getMessage());
                        return new ExampleDto();
                    }
                })
                .toList();
    }

    public void deleteById(String id) {
        requireNonNull(id);

        try {
            hashOperations.delete(Hash_KEY, id);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }
}
