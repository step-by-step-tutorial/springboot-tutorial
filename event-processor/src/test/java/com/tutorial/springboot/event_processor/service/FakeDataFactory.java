package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;

public class FakeDataFactory {
        static final EventModel FAKE_EVENT_MODEL = new EventModel("fake text");
        static final EventModel NULL_EVENT_MODEL = null;
    }
