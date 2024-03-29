package com.tutorial.springboot.logger_log4j2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("unit tests of console logging")
class ApplicationTest {

    Logger logger = LoggerFactory.getLogger(ApplicationTest.class.getSimpleName());

    @Test
    @DisplayName("writing a message on the console")
    void shouldBeWrittenMessageOnTheConsole() {
        assertDoesNotThrow(() -> {
            logger.error("error log");
            logger.warn("warn log");
            logger.info("info log");
            logger.debug("debug log");
            logger.trace("trace log");
        });
    }

}
