package com.tutorial.springboot.streaming_kafka.sink;

import org.apache.kafka.streams.kstream.KStream;

public abstract class AbstractSink<K, V> implements Sink<K, V> {

    protected final String topic;

    protected AbstractSink(String topic) {
        this.topic = topic;
    }

    @Override
    public void push(KStream<K, V> stream) {
        stream.to(topic);
    }

}
