package com.tutorial.springboot.messagingactivemq;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.synchronizedMap;

@EnableAspectJAutoProxy
@Aspect
@Component
public class MessagePublisherWatcher {

    private final Map<Integer, List<KeyValue<Integer, Serializable>>> transactions = synchronizedMap(new HashMap<>());

    private final AtomicInteger keyGenerator = new AtomicInteger(1);

    public void reset() {
        transactions.clear();
        keyGenerator.set(1);
    }

    public Map<Integer, List<KeyValue<Integer, Serializable>>> getTransactions() {
        return transactions;
    }

    @Before("execution(* com.tutorial.springboot.messagingactivemq.service.StringMessagePublisher.publish(..)) && args(message)")
    public void before(JoinPoint joinPoint, Serializable message) {
        int key = keyGenerator.get();
        List<KeyValue<Integer, Serializable>> messages = new ArrayList<>();
        messages.add(new KeyValue<>(key, message));
        transactions.put(key, messages);
    }

    @AfterThrowing(
            pointcut = "execution(* com.tutorial.springboot.messagingactivemq.service.StringMessagePublisher.publish(..)) && args(message)",
            throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Serializable message, Exception exception) {
        int key = keyGenerator.get();
        transactions.get(key).add(new KeyValue<>(key, exception));
    }

    @After("execution(* com.tutorial.springboot.messagingactivemq.service.StringMessagePublisher.publish(..)) && args(message)")
    public void after(JoinPoint joinPoint, Serializable message) {
        int key = keyGenerator.getAndIncrement();
        transactions.get(key).add(new KeyValue<>(key, message));
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