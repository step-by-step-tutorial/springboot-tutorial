package com.tutorial.springboot.logger_log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class.getSimpleName());

    public static void main(String[] args) {
        logger.error("If log level is ERROR => log errors");
        logger.warn("If log level is WARN => log errors, warns");
        logger.info("If log level is INFO => log errors, warns, info");
        logger.debug("If log level is DEBUG => log errors, warns, info, debug");
        logger.trace("If log level is TRACE => log log errors, warns, info, debug, trace");
    }
}
