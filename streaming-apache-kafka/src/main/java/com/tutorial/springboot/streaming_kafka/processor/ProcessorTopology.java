package com.tutorial.springboot.streaming_kafka.processor;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ProcessorTopology {

    public ProcessorTopology() {
    }

    @SafeVarargs
    public final void build(
            SourceProcessor<Serializable, Serializable> sourceProcessor,
            SinkProcessor<Serializable, Serializable> sinkProcessor,
            StreamProcessor<Serializable, Serializable, Serializable, Serializable>... streamProcessors
    ) {
        KStream<Serializable, Serializable> input = sourceProcessor.startListening();
        KStream<Serializable, Serializable> result = null;

        for (StreamProcessor<Serializable, Serializable, Serializable, Serializable> processor : streamProcessors) {
            if (result == null) {
                result = processor.process(input);
            } else {
                result = processor.process(result);
            }
        }

        sinkProcessor.push(result);
    }
}
