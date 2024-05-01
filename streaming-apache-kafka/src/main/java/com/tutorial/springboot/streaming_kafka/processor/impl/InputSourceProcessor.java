package com.tutorial.springboot.streaming_kafka.processor.impl;

import com.tutorial.springboot.streaming_kafka.processor.SourceProcessor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InputSourceProcessor implements SourceProcessor<String, String> {

    @Value("${topic.input}")
    private String inputTopic;

    private final StreamsBuilder builder;

    public InputSourceProcessor(StreamsBuilder builder) {
        this.builder = builder;
    }

    @Override
    public KStream<String, String> startListening() {
        return builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));
    }
}
