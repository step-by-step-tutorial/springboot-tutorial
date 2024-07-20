package com.tutorial.springboot.streaming_kafka.character_counter;

import com.tutorial.springboot.streaming_kafka.source.AbstractSource;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CharacterCounterSource extends AbstractSource<Integer, String> {

    public CharacterCounterSource(@Value("${topic.source}") String source, StreamsBuilder builder) {
        super(source, builder);
    }

}
