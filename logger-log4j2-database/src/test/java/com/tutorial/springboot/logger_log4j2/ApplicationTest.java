package com.tutorial.springboot.logger_log4j2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.tutorial.springboot.logger_log4j2.JdbcUtils.executeQuery;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTest {

    static final String LOGGER_NAME = "TEST_DATABASE_APPENDER_LOGGER";

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

        var actual = executeQuery(statement -> {
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
