package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class EventHandler {

    private final Logger logger = LoggerFactory.getLogger(EventHandler.class.getSimpleName());

    @EventListener
    void onEvent(final EventModel event) {
        requireNonNull(event, "event should not be null");
        logger.info("message received: {}", event.text());
    }

}
