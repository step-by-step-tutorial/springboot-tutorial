//package com.tutorial.springboot.streaming_kafka.processor.impl;
//
//import com.tutorial.springboot.streaming_kafka.processor.SinkProcessor;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.kstream.KStream;
//import org.apache.kafka.streams.kstream.Produced;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OutputSinkProcessor implements SinkProcessor<String, Long> {
//
//    @Value("${topic.sink}")
//    private String outputTopic;
//
//    public OutputSinkProcessor() {
//    }
//
//    @Override
//    public void push(KStream<String, Long> stream) {
//        stream.to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));
//    }
//}
