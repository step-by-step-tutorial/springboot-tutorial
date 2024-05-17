package com.tutorial.springboot.event_processor.util;

public record LogModel(LogTopic topic, String message) {
}
