package com.tutorial.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MessageDispatcher {
    private final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class.getSimpleName());

    @Autowired
    private ApplicationEventPublisher publisher;

    public void dispatch(MessageEvent event) {
        publisher.publishEvent(event);
        logger.info("sent message: {}", event.text());
    }

}
