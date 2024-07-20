package com.tutorial.springboot.streaming_kafka;

import com.tutorial.springboot.streaming_kafka.topic.SourceTopicProducer;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SampleGenerator {

    private static final int MESSAGE_NUMBER = 10;

    private static final int MESSAGE_LENGTH = 10;


    public SampleGenerator(SourceTopicProducer sourceTopicProducer) {
        for (int i = 0; i < MESSAGE_NUMBER; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sourceTopicProducer.push(generateRandomString(MESSAGE_LENGTH));
        }
    }

    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }
}
