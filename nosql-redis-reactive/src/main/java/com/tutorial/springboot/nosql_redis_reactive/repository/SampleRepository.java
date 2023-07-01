package com.tutorial.springboot.nosql_redis_reactive.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.springboot.nosql_redis_reactive.entity.SampleModel;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SampleRepository {

    private final Logger logger = LoggerFactory.getLogger(SampleRepository.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Resource(name = "redisTemplate")
    private ReactiveRedisOperations<String, String> template;

    public Optional<String> save(SampleModel model) {
        Objects.requireNonNull(model);

        model.setId(UUID.randomUUID().toString());

        try {
            template.opsForValue()
                    .setIfAbsent(model.getId(), mapper.writeValueAsString(model))
                    .subscribe();
            return Optional.ofNullable(model.getId());
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return Optional.<String>empty();
        }
    }

    public Optional<SampleModel> findByKey(String key) {
        Objects.requireNonNull(key);

        try {
            String model = template.opsForValue()
                    .get(key)
                    .block();
            return Optional.ofNullable(mapper.readValue(model, SampleModel.class));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return Optional.empty();
        }
    }

    public void update(SampleModel model) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(model.getId());

        try {
            template.opsForValue()
                    .set(model.getId(), mapper.writeValueAsString(model))
                    .subscribe();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public void deleteById(String id) {
        Objects.requireNonNull(id);
        template.opsForValue()
                .delete(id)
                .subscribe();
    }

    public List<SampleModel> findAll(Collection<String> keys) {
        Objects.requireNonNull(keys);

        return template.opsForValue()
                .multiGet(keys)
                .blockOptional()
                .orElse(new ArrayList<>())
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

    public List<SampleModel> findAll() {
        return findAll(
                template.keys("*")
                        .collectList()
                        .block()
        );
    }
}
