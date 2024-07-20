package com.tutorial.springboot.streaming_kafka.sink;

import org.apache.kafka.streams.kstream.KStream;

public interface Sink<K, V> {
    void push(KStream<K, V> stream);
}
