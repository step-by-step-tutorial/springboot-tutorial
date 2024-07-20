package com.tutorial.springboot.streaming_kafka.uppercase_transformer;

import com.tutorial.springboot.streaming_kafka.processor.StreamProcessor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

@Component
public class UppercaseTransformer implements StreamProcessor<Integer, String, Integer, String> {

    public UppercaseTransformer() {
    }

    @Override
    public KStream<Integer, String> process(KStream<Integer, String> stream) {
        return stream.map((key, value) -> new KeyValue<>(key, value.toUpperCase()));
    }

}
