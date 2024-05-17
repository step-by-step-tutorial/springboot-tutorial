package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;
import com.tutorial.springboot.event_processor.util.LogModel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.event_processor.util.LogTopic.INPUT_EVENT;
import static com.tutorial.springboot.event_processor.util.LogUtils.logInfo;
import static java.util.Objects.requireNonNull;

@Component
public class EventSubscriber {

    @EventListener
    void onEvent(final EventModel event) {
        requireNonNull(event, "Event should not be null");
        logInfo(new LogModel(INPUT_EVENT, event.content()));
    }
}
