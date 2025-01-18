package com.tutorial.springboot.cdcdebezium;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles({"test", "redis", "h2"})
class RedisTest {

    static final Logger LOGGER = LoggerFactory.getLogger(RedisTest.class.getSimpleName());

    @Container
    static final RedisContainer REDIS = new RedisContainer("redis:latest");

    @SuppressWarnings({"unused"})
    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", REDIS::getRedisPort);
    }

    @BeforeAll
    static void start() {
        REDIS.start();
    }

    @AfterAll
    static void stop() {
        REDIS.stop();
    }

    @Test
    void contextLoads() {
    }

}
