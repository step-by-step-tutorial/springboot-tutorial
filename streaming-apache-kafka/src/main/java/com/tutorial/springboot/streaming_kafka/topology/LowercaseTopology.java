package com.tutorial.springboot.streaming_kafka.topology;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class LowercaseTopology {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Value("${topic.source}")
    private String source;

    @Value("${topic.sink}")
    private String sink;

    @Autowired
    public void build(StreamsBuilder streamsBuilder) {
        logger.info("Building topology {}", this.getClass().getSimpleName());

        streamsBuilder.<Integer, String>stream(source)
                .map((key, value) -> new KeyValue<>(key, value.toLowerCase()))
                .to(sink, Produced.with(Serdes.Integer(), new JsonSerde<>()));
    }
}
