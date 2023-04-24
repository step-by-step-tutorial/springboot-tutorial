package com.tutorial.springboot.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("listener: {@link MessageListener} unit tests")
class ListenerTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageListenerWatcher watcher;

    @BeforeEach
    void init() {
        watcher.reset();
    }

    @Test
    @DisplayName("when an event is published then the listener should receive the event")
    void whenEventPublish_ThenListenerShouldReceiveTheEvent() {
        var givenMessageEvent = new MessageEvent("Listener:Test Message");

        publisher.publishEvent(givenMessageEvent);

        assertEquals(1, watcher.getEvents().size());
        assertEquals(1, watcher.getEvents().get(1).key);
        assertEquals(givenMessageEvent.text(), watcher.getEvents().get(1).value.text());
    }

    @Test
    @DisplayName("when two events are published then listener should receive the events")
    void whenTwoEventsPublish_ThenListenerShouldReceiveTwoEvents() {
        var givenMessageEvent1 = new MessageEvent("Listener:Test Message 1");
        var givenMessageEvent2 = new MessageEvent("Listener:Test Message 2");

        publisher.publishEvent(givenMessageEvent1);
        publisher.publishEvent(givenMessageEvent2);

        assertEquals(2, watcher.getEvents().size());
        assertEquals(1, watcher.getEvents().get(1).key);
        assertEquals(givenMessageEvent1.text(), watcher.getEvents().get(1).value.text());
        assertEquals(2, watcher.getEvents().get(2).key);
        assertEquals(givenMessageEvent2.text(), watcher.getEvents().get(2).value.text());
    }

}
