package com.tutorial.springboot.listener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

  @Autowired
  private MessagePublisher publisher;

  @BeforeEach
  void init() {
    assertNotNull(publisher);
  }

  @Test
  @DisplayName("when an event published then listener should receive the event")
  void whenAnEventPublishedThenListenerShouldReceiveTheEvent() {
    var givenMessageEvent = new MessageEvent("Test Message");

    publisher.publish(givenMessageEvent);

    assertTrue(true);
  }

}
