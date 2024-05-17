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
            logger.trace("\tIf log level is OFF \t=> log nothing");
            logger.debug("\tIf log level is ERROR \t=> log errors");
            logger.info("\tIf log level is WARNm \t=> log errors, warns");
            logger.warn("\tIf log level is INFO \t=> log errors, warns, info");
            logger.error("\tIf log level is DEBUG \t=> log errors, warns, info, debug");
        });
    }

}
