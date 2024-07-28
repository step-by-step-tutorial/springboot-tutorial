package com.tutorial.springboot.event_processor.model;

public enum LogTopic {
    INPUT_EVENT("Event is subscribed"),
    OUTPUT_EVENT("Event is published"),
    ;

    private final String message;

    LogTopic(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
