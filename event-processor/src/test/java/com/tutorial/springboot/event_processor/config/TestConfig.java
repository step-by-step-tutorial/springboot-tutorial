package com.tutorial.springboot.event_processor.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public LogCapture logInfoCapture() {
        final var logCapture = new LogCapture();
        logCapture.start();

        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration configuration = loggerContext.getConfiguration();
        LoggerConfig rootLoggerConfig = configuration.getLoggerConfig("");
        rootLoggerConfig.addAppender(logCapture, Level.INFO, null);

        return logCapture;
    }

}
