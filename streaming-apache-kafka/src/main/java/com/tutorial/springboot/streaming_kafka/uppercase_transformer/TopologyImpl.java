package com.tutorial.springboot.streaming_kafka.uppercase_transformer;

import com.tutorial.springboot.streaming_kafka.topology.AbstractTopology;
import org.springframework.stereotype.Component;

@Component
public class TopologyImpl extends AbstractTopology<Integer, String, Integer, String> {

    public TopologyImpl(SourceImpl source, SinkImpl sink, UppercaseTransformer uppercaseTransformer) {
        super(source, sink, uppercaseTransformer);
    }
}
