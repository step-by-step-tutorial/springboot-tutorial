package com.tutorial.springboot.streaming_kafka.character_counter;

import com.tutorial.springboot.streaming_kafka.sink.AbstractSink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CharacterCounterSink extends AbstractSink<String, String> {

    public CharacterCounterSink(@Value("${topic.sink}") String topic) {
        super(topic);
    }

}
