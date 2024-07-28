package com.tutorial.springboot.event_processor.model;

public record LogModel(LogTopic topic, String message) {
}
