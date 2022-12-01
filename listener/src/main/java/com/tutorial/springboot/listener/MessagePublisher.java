package com.tutorial.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {
  private final Logger logger = LoggerFactory.getLogger(MessagePublisher.class.getSimpleName());

  @Autowired
  private ApplicationEventPublisher publisher;

  public void publish(MessageEvent event) {
    publisher.publishEvent(event);
    logger.info("sent message: {}", event.getText());
  }

}
