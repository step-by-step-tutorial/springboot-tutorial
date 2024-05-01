package com.tutorial.springboot.streaming_kafka.processor;

import org.apache.kafka.streams.kstream.KStream;

import java.io.Serializable;

public interface SourceProcessor<K extends Serializable, V extends Serializable> {
    KStream<K, V> startListening();
}
