package com.tutorial.springboot.messagingactivemq;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.synchronizedMap;

@EnableAspectJAutoProxy
@Aspect
@Component
public class MessageHandlerWatcher {

    private final Map<Integer, KeyValue<Integer, Serializable>> messages = synchronizedMap(new HashMap<>());

    private final AtomicInteger keyGenerator = new AtomicInteger(1);

    public void reset() {
        messages.clear();
        keyGenerator.set(1);
    }

    public Map<Integer, KeyValue<Integer, Serializable>> getMessages() {
        return messages;
    }

    @Before("execution(* com.tutorial.springboot.messagingactivemq.service.StringMessageHandler.onMessage(..)) && args(message)")
    public void before(JoinPoint joinPoint, Serializable message) {
        int key = keyGenerator.getAndIncrement();
        messages.put(key, new KeyValue<>(key, message));
    }

    @After("execution(* com.tutorial.springboot.messagingactivemq.service.StringMessageHandler.onMessage(..)) && args(message)")
    public void after(JoinPoint joinPoint, Serializable message) {
        int key = keyGenerator.getAndIncrement();
        messages.replace(key, new KeyValue<>(key, message));
    }

    static class KeyValue<T, V> {
        public T key;

        public V value;

        public KeyValue(T key, V value) {
            this.key = key;
            this.value = value;
        }

    }
}