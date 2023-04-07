# <p align="center">Listener</p>

This sample is about working event and event listener in Spring Boot. There are three concepts `event`, `listener` and
`publish` for the [event driven](https://github.com/oss-academy/article/blob/main/event-driven/README.md) mechanism.

## Event

There are two mechanism in Spring to define an event. One solution is, create a java bean and another one is, create a
class extended from `ApplicationEvent`.

**Java Bean**

```java
public class SampleEvent {
}
```

**ApplicationEvent**

```java

import org.springframework.context.ApplicationEvent;

public class SampleEvent extends ApplicationEvent {

  public MessageEvent(Object source) {
    super(source);
  }
}
```

## Listener

There are two method to implement a listener, annotation driven and programmatically. Also, it is possible to have async
listener with using `@Async`.

### Annotation Driven

```java
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SampleListener {

  @EventListener
  void onMessage(SampleEvent event) {
  }
}
```

```java
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SampleListener {

  @Async
  @EventListener
  void onMessage(SampleEvent event) {
  }
}
```

### Programmatically

```java
import org.springframework.context.ApplicationListener;

@Component
class SampleListener implements ApplicationListener<SampleEvent> {

  @Override
  public void onApplicationEvent(SampleEvent event) {
  }
}
```

## Publisher

Publisher is used to publish/dispatch events and, spring has a class named `ApplicationEventPublisher` to do that.

```java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SampleDispatcher {

  @Autowired
  private ApplicationEventPublisher publisher;

  public void dispatch(SampleEvent event) {
    publisher.publishEvent(event);
  }

}
```

## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)

## Build

```bash
mvn clean package -DskipTests=true
```

## Test

```bash
mvn  test
```

## Run

```bash
mvn  spring-boot:run
```

**<p align="center"> [Top](#Listener) </p>**