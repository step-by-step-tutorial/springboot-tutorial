package com.tutorial.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class MessageListener {

    private final Logger logger = LoggerFactory.getLogger(MessageListener.class.getSimpleName());

    @EventListener
    void onMessage(MessageEvent event) {
        requireNonNull(event);

        logger.info("received message: {}", event.text());
    }
}
