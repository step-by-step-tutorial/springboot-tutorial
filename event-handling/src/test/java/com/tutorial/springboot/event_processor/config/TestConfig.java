package com.tutorial.springboot.event_processor.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public TestLogRepository createTestLogRepositoryBean() {
        final var appender = new TestLogRepository();
        appender.start();

        ((LoggerContext) LogManager.getContext(false))
                .getConfiguration()
                .getLoggerConfig("")
                .addAppender(appender, Level.INFO, null);

        return appender;
    }

}
