package com.tutorial.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private final Logger logger = LoggerFactory.getLogger(MessageListener.class.getSimpleName());

    @EventListener
    void onMessage(MessageEvent event) {
        logger.info("received message: {}", event.text());
    }
}
