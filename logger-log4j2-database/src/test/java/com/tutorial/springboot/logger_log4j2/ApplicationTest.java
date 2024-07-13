package com.tutorial.springboot.logger_log4j2;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.tutorial.springboot.logger_log4j2.FileUtils.readFileContent;
import static com.tutorial.springboot.logger_log4j2.JdbcUtils.executeQuery;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DisplayName("unit tests of database[mysql] logging")
class ApplicationTest {

    static final String LOGGER_NAME = "TEST_DATABASE_APPENDER_LOGGER";

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0");

    static {
        try {
            MYSQL.withDatabaseName("test_db")
                    .withUsername("user")
                    .withPassword("password")
                    .withExposedPorts(3306)
                    .withCreateContainerCmdModifier(cmd -> cmd.withPortBindings(new PortBinding(Ports.Binding.bindPort(3306), new ExposedPort(3306))))
                    .start();

            var result = executeQuery(MYSQL, statement -> statement.execute(readFileContent("schema.sql")));
            assertTrue(result.isPresent());
            assertFalse(result.get());
        } catch (Exception e) {
            System.err.println(String.format("Start mysql container failed due to: %s", e.getMessage()));
        }
    }


    @AfterAll
    static void afterAll() {
        try {
            MYSQL.stop();
        } catch (Exception e) {
            System.err.println(String.format("Stop mysql container failed due to: %s", e.getMessage()));
        }
    }

    @Test
    @DisplayName("writing a message on the database")
    void shouldBeWrittenMessageOnTheFile() {
        var givenLogger = LoggerFactory.getLogger(LOGGER_NAME);

        //When
        givenLogger.error("errors");
        givenLogger.warn("errors, warns");
        givenLogger.info("errors, warns, info");
        givenLogger.debug("errors, warns, info, debug");
        givenLogger.trace("errors, warns, info, debug, trace");

        var actual = executeQuery(MYSQL, statement -> {
            var resultSet = statement.executeQuery(String.format("SELECT COUNT(*) as row_count FROM LOG_TABLE WHERE logger = '%s'", LOGGER_NAME));
            resultSet.next();
            long count = resultSet.getLong("row_count");
            resultSet.close();
            return count;
        });

        assertTrue(actual.isPresent());
        assertEquals(3, actual.get());

    }

}
