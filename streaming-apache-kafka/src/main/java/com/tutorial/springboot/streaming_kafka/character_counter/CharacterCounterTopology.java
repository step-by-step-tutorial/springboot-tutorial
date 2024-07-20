package com.tutorial.springboot.streaming_kafka.character_counter;

import com.tutorial.springboot.streaming_kafka.topology.AbstractTopology;
import org.springframework.stereotype.Component;

@Component
public class CharacterCounterTopology extends AbstractTopology<Integer, String, String, String> {

    public CharacterCounterTopology(CharacterCounterSource source, CharacterCounterSink sink, CharacterCounter uppercaseTransformer) {
        super(source, sink, uppercaseTransformer);
    }
}
