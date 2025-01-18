package com.tutorial.springboot.event_processor.model;

public enum LogSubject {
    INPUT_EVENT("Event is subscribed"),
    OUTPUT_EVENT("Event is published"),
    ;

    private final String message;

    LogSubject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
