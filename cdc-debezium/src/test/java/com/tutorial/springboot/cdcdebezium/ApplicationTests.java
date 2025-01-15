package com.tutorial.springboot.cdcdebezium;

import com.tutorial.springboot.cdcdebezium.entity.SampleEntity;
import com.tutorial.springboot.cdcdebezium.repository.JdbcSampleRepository;
import com.tutorial.springboot.cdcdebezium.service.MainTopicService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles({"test", "mysql", "embedded-kafka", "embedded-debezium"})
@Testcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {
        "listeners=PLAINTEXT://:9092",
        "port=9092"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
@Transactional
class ApplicationTests {

    static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTests.class.getSimpleName());

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0");

    static {
        try {
            MYSQL.withDatabaseName("test_db")
                    .withUsername("root")
                    .withPassword("password")
                    .withInitScript("init.sql");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @SuppressWarnings({"unused"})
    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
    }

    @BeforeAll
    static void start() {
        MYSQL.start();
    }

    @AfterAll
    static void stop() {
        MYSQL.stop();
    }

    @Autowired
    private JdbcSampleRepository jdbcSampleRepository;

    @Autowired
    private MainTopicService mainTopicService;

    @Test
    void contextLoads() {
        jdbcSampleRepository.save(new SampleEntity().setCode(1).setName("test name").setDatetime(LocalDateTime.now()));
        mainTopicService.push("test message");
    }

}
