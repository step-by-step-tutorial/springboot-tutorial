package com.tutorial.springboot.streaming_kafka.character_counter;

import com.tutorial.springboot.streaming_kafka.processor.StreamProcessor;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CharacterCounter implements StreamProcessor<Integer, String, String, String> {

    public CharacterCounter() {
    }

    @Override
    public KStream<String, String> process(KStream<Integer, String> stream) {
        return stream.flatMapValues(value -> Arrays.asList(value.split("")))
                .groupBy((key, value) -> value)
                .count()
                .toStream()
                .mapValues((key, value) -> key + ":" + value);
    }

}
