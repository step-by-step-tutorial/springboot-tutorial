package com.tutorial.springboot.logger_log4j2;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ApplicationTest {

    Logger logger = LoggerFactory.getLogger(ApplicationTest.class.getSimpleName());

    @Test
    void shouldBeWrittenMessageOnTheFile() {
        assertDoesNotThrow(() -> {
            logger.info("info log");
            logger.error("error log");
            logger.warn("warn log");
            logger.debug("debug log");
            logger.trace("trace log");
        });
    }
}
