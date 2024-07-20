package com.tutorial.springboot.streaming_kafka.source;

import org.apache.kafka.streams.kstream.KStream;

public interface Source<K, V> {
    KStream<K, V> startListening();
}
