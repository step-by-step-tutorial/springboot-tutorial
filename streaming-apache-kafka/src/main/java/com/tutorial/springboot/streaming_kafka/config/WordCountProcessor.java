package com.tutorial.springboot.streaming_kafka.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class WordCountProcessor {

    @Value("${topic.source}")
    private String source;

    @Value("${topic.sink}")
    private String sink;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<Integer, String> stream = streamsBuilder.stream(source);
        stream.map(this::uppercaseValue).to(sink, Produced.with(Serdes.Integer(), new JsonSerde<>()));
    }

    private KeyValue<Integer, String> uppercaseValue(Integer key, String value) {
        return new KeyValue<>(key, value.toUpperCase());
    }
}