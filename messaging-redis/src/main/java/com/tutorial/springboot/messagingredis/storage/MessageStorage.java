package com.tutorial.springboot.messagingredis.storage;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MessageStorage {

    private final AtomicInteger messageIdSequence = new AtomicInteger(0);

    private final Map<Integer, String> messages = Collections.synchronizedMap(new HashMap<>());

    public MessageStorage() {
    }

    public void insert(String message) {
        messages.putIfAbsent(messageIdSequence.incrementAndGet(), message);
    }

    public String selectById(int id) {
        return messages.get(id);
    }

    public List<String> selectAll() {
        return new ArrayList<>(messages.values());
    }

    public int count() {
        return messages.size();
    }

    public void updateById(int id, String message) {
        messages.put(id, message);
    }

    public void deleteById(int id) {
        messages.remove(id);
    }

    public void truncate() {
        messages.clear();
    }

}