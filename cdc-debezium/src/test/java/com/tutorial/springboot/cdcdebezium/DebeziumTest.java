package com.tutorial.springboot.cdcdebezium;

import com.tutorial.springboot.cdcdebezium.entity.Example;
import com.tutorial.springboot.cdcdebezium.repository.JdbcExampleRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles({"test", "mysql", "debezium"})
class DebeziumTest {

    @Container
    public static ComposeContainer ENVIRONMENT = new ComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
            .waitingFor("mysql", Wait.forListeningPort()
                    .withStartupTimeout(Duration.ofMinutes(2)));

    @BeforeAll
    static void start() {
        ENVIRONMENT.start();
    }

    @AfterAll
    static void stop() {
        ENVIRONMENT.stop();
    }

    @Autowired
    private JdbcExampleRepository jdbcExampleRepository;

    @Test
    void contextLoads() {
        jdbcExampleRepository.save(new Example().setCode(1).setName("test name").setDatetime(LocalDateTime.now()));
    }

}
