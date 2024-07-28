package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;
import com.tutorial.springboot.event_processor.model.LogModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.event_processor.model.LogTopic.OUTPUT_EVENT;
import static com.tutorial.springboot.event_processor.util.LogUtils.logInfo;
import static java.util.Objects.requireNonNull;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher publisher;

    public EventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(final EventModel event) {
        requireNonNull(event, "Event should not be null");

        publisher.publishEvent(event);
        logInfo(new LogModel(OUTPUT_EVENT, event.content()));
    }
}
