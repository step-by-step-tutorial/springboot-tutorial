package com.tutorial.springboot.event_processor.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.util.ArrayList;
import java.util.List;

public class TestLogRepository extends AbstractAppender {

    private final List<LogEvent> events = new ArrayList<>();

    public TestLogRepository() {
        super("test", null, null, false, null);
    }

    @Override
    public void append(final LogEvent event) {
        events.add(event);
    }

    public List<String> getMessages() {
        return events.stream()
                .map(e -> e.getMessage().getFormattedMessage())
                .toList();
    }
}
