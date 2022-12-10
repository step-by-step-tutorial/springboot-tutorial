package com.tutorial.springboot.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RecordApplicationEvents
class DispatcherTest {

    @Autowired
    private MessageDispatcher dispatcher;

    @Autowired
    private MessageDispatcherWatcher watcher;

    @BeforeEach
    void init() {
        watcher.reset();
    }

    @Test
    @DisplayName("when an event published then listener should receive the event")
    void whenAnEventPublishedThenDispatcherShouldSendTheEvent() {
        var givenMessageEvent = new MessageEvent("Dispatcher:Test Message");
        dispatcher.dispatch(givenMessageEvent);


        assertEquals(1, watcher.getEvents().size());
        assertEquals(1, watcher.getEvents().get(1).key);
        assertEquals(givenMessageEvent.text(), watcher.getEvents().get(1).value.text());
    }

    @Test
    @DisplayName("when an event published then listener should receive the event")
    void whenTwoEventPublishedThenDispatcherShouldSendTwoEvent() {
        var givenMessageEvent1 = new MessageEvent("Dispatcher:Test Message 1");
        var givenMessageEvent2 = new MessageEvent("Dispatcher:Test Message 2");

        dispatcher.dispatch(givenMessageEvent1);
        dispatcher.dispatch(givenMessageEvent2);


        assertEquals(2, watcher.getEvents().size());
        assertEquals(1, watcher.getEvents().get(1).key);
        assertEquals(givenMessageEvent1.text(), watcher.getEvents().get(1).value.text());
        assertEquals(2, watcher.getEvents().get(2).key);
        assertEquals(givenMessageEvent2.text(), watcher.getEvents().get(2).value.text());
    }

}
