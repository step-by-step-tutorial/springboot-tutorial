package com.tutorial.springboot.listener;

import static java.util.Collections.synchronizedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@EnableAspectJAutoProxy
@Aspect
@Component
public class MessageDispatcherWatcher {

  private final Map<Integer, KeyValue<Integer, MessageEvent>> events = synchronizedMap(new HashMap<>());

  private final AtomicInteger keyGenerator = new AtomicInteger(1);

  public void reset() {
    events.clear();
    keyGenerator.set(1);
  }

  public Map<Integer, KeyValue<Integer, MessageEvent>> getEvents() {
    return events;
  }

  @Before("execution(* com.tutorial.springboot.listener.MessageDispatcher.dispatch(..)) && args(event)")
  public void before(JoinPoint joinPoint, MessageEvent event) {
    events.put(keyGenerator.get(), new KeyValue<>(0, event));
  }

  @After("execution(* com.tutorial.springboot.listener.MessageDispatcher.dispatch(..)) && args(event)")
  public void after(JoinPoint joinPoint, MessageEvent event) {
    events.replace(keyGenerator.get(), new KeyValue<>(keyGenerator.getAndIncrement(), event));
  }

  class KeyValue<T, V> {
    public T key;

    public V value;

    public KeyValue(T key, V value) {
      this.key = key;
      this.value = value;
    }

  }
}