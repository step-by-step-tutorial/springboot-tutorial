package com.tutorial.springboot.streaming_kafka.topology;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CharacterCounterTopology {

    @Value("${topic.source}")
    private String source;

    @Value("${topic.sink}")
    private String sink;

    @Autowired
    public void build(StreamsBuilder streamsBuilder) {
        streamsBuilder.<Integer, String>stream(source)
                .flatMapValues(value -> Arrays.asList(value.split("")))
                .groupBy((key, value) -> value)
                .count()
                .toStream()
                .mapValues((key, value) -> key + ":" + value)
                .to(sink, Produced.with(Serdes.String(), new JsonSerde<>()));
    }
}
