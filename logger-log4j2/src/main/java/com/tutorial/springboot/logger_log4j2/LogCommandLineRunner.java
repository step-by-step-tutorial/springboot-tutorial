package com.tutorial.springboot.logger_log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LogCommandLineRunner implements CommandLineRunner {
    static final Logger logger = LoggerFactory.getLogger(LogCommandLineRunner.class.getSimpleName());

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            logger.info("test log {}", i);
        }
    }
}