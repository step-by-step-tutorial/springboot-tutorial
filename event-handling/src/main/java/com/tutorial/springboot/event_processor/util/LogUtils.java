package com.tutorial.springboot.event_processor.util;

import com.tutorial.springboot.event_processor.model.LogModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger("Event Handler");

    private LogUtils() {
    }

    public static void logInfo(LogModel model) {
        logger.info(String.format("%s: %s", model.subject().getMessage(), model.message()));
    }
}
