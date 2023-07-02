package com.tutorial.springboot.event_processor.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.util.ArrayList;
import java.util.List;

public class LogCapture extends AbstractAppender {

    private final List<LogEvent> events = new ArrayList<>();

    public LogCapture() {
        super("test", null, null, false, null);
    }

    @Override
    public void append(final LogEvent event) {
        events.add(event);
    }

    public List<String> getEvents() {
        return events.stream()
                .map(e -> e.getMessage().getFormattedMessage())
                .toList();
    }
}
