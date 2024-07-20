package com.tutorial.springboot.streaming_kafka.source;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

public abstract class AbstractSource<K, V> implements Source<K, V> {

    protected final StreamsBuilder builder;

    protected final String topic;

    protected AbstractSource(String topic, StreamsBuilder builder) {
        this.topic = topic;
        this.builder = builder;
    }

    @Override
    public KStream<K, V> startListening() {
        return builder.stream(topic);
    }

}
