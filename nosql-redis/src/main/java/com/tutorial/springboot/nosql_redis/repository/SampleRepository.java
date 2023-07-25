package com.tutorial.springboot.nosql_redis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.springboot.nosql_redis.model.SampleModel;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Repository
public class SampleRepository {

    private static final String Hash_KEY = "Sample";

    private final Logger logger = LoggerFactory.getLogger(SampleRepository.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    public Optional<String> save(SampleModel model) {
        requireNonNull(model);

        model.setId(UUID.randomUUID().toString());

        try {
            hashOperations.putIfAbsent(Hash_KEY, model.getId(), mapper.writeValueAsString(model));
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
            hashOperations.put(Hash_KEY, model.getId(), mapper.writeValueAsString(model));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public Optional<SampleModel> findById(String id) {
        requireNonNull(id);

        try {
            return Optional.ofNullable(mapper.readValue(hashOperations.get(Hash_KEY, id), SampleModel.class));
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
                        return mapper.readValue(item, SampleModel.class);
                    } catch (Exception exception) {
                        logger.error(exception.getMessage());
                        return new SampleModel();
                    }
                })
                .toList();
    }

    public void deleteById(String id) {
        requireNonNull(id);

        hashOperations.delete(Hash_KEY, id);
    }
}
