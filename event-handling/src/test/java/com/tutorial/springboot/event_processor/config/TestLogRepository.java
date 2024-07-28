package com.tutorial.springboot.event_processor.config;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayList;
import java.util.List;

@Plugin(name = "TestLogRepository", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class TestLogRepository extends AbstractAppender {

    private final List<String> events = new ArrayList<>();

    public TestLogRepository(String name) {
        super(name, null, PatternLayout.createDefaultLayout(), false, new Property[0]);
    }

    @Override
    public void append(final LogEvent event) {
        events.add(event.getMessage().getFormattedMessage());
    }

    public List<String> getMessages() {
        return events;
    }

    @PluginFactory
    public static TestLogRepository createAppender(@PluginAttribute("name") String name) {
        return new TestLogRepository(name);
    }

}
