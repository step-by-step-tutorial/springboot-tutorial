package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class EventPublisher {
    private final Logger logger = LoggerFactory.getLogger(EventPublisher.class.getSimpleName());

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publish(EventModel event) {
        requireNonNull(event, "event should not be null");

        publisher.publishEvent(event);
        logger.info("message sent: {}", event.text());
    }

}
