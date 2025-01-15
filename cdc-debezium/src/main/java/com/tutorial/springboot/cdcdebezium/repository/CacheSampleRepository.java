package com.tutorial.springboot.cdcdebezium.repository;

import com.tutorial.springboot.cdcdebezium.model.SampleModel;
import jakarta.annotation.PreDestroy;
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
public class CacheSampleRepository {

    private static final String Hash_KEY = "Sample";

    private final Logger logger = LoggerFactory.getLogger(JdbcSampleRepository.class);

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    public Optional<String> save(SampleModel model) {
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

    public void update(SampleModel model) {
        requireNonNull(model);
        requireNonNull(model.getId());

        try {
            hashOperations.put(Hash_KEY, model.getId(), toJson(model));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public Optional<SampleModel> findById(String id) {
        requireNonNull(id);

        try {
            final var jsonString = hashOperations.get(Hash_KEY, id);
            return Optional.ofNullable(toModel(jsonString, SampleModel.class));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return Optional.empty();
        }
    }

    public List<SampleModel> findAll() {
        return hashOperations.entries(Hash_KEY)
                .values()
                .stream()
                .map(item -> {
                    try {
                        return toModel(item, SampleModel.class);
                    } catch (Exception exception) {
                        logger.error(exception.getMessage());
                        return new SampleModel();
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
