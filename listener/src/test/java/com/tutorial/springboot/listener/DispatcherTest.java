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
@DisplayName("listener: {@link MessageDispatcher} unit tests")
class DispatcherTest {

    @Autowired
    private MessageDispatcher underTest;

    @Autowired
    private MessageDispatcherWatcher watcher;

    @BeforeEach
    void init() {
        watcher.reset();
    }

    @Test
    @DisplayName("when an event is dispatched then the event is accessible in the environment")
    void whenEventDispatch_ThenEventShouldBePublishedThroughoutEnvironment() {
        var givenMessageEvent = new MessageEvent("Dispatcher: Test Message");

        underTest.dispatch(givenMessageEvent);

        assertEquals(1, watcher.getEvents().size());
        assertEquals(1, watcher.getEvents().get(1).key);
        assertEquals(givenMessageEvent.text(), watcher.getEvents().get(1).value.text());
    }

    @Test
    @DisplayName("when two events are dispatched then the events are accessible in the environment")
    void whenTwoEventsDispatch_ThenEventsShouldBePublishedThroughoutEnvironment() {
        var givenMessageEvent1 = new MessageEvent("Dispatcher: Test Message 1");
        var givenMessageEvent2 = new MessageEvent("Dispatcher: Test Message 2");

        underTest.dispatch(givenMessageEvent1);
        underTest.dispatch(givenMessageEvent2);


        assertEquals(2, watcher.getEvents().size());
        assertEquals(1, watcher.getEvents().get(1).key);
        assertEquals(givenMessageEvent1.text(), watcher.getEvents().get(1).value.text());
        assertEquals(2, watcher.getEvents().get(2).key);
        assertEquals(givenMessageEvent2.text(), watcher.getEvents().get(2).value.text());
    }

}
