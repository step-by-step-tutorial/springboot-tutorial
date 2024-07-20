package com.tutorial.springboot.streaming_kafka.topology;

import com.tutorial.springboot.streaming_kafka.processor.StreamProcessor;
import com.tutorial.springboot.streaming_kafka.sink.Sink;
import com.tutorial.springboot.streaming_kafka.source.Source;

public abstract class AbstractTopology<IK, IV, OK, OV> {

    protected AbstractTopology(
            Source<IK, IV> source,
            Sink<OK, OV> sink,
            StreamProcessor<IK, IV, OK, OV> processor
    ) {
        sink.push(processor.process(source.startListening()));
    }
}
