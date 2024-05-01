package com.tutorial.springboot.streaming_kafka.processor;

import org.apache.kafka.streams.kstream.KStream;

import java.io.Serializable;

public interface SinkProcessor<K extends Serializable, V extends Serializable> {
    void push(KStream<K, V> stream);
}
