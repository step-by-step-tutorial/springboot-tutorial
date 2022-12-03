# <p align="center">Listener</p>

This sample is about working event and event listener in Spring Boot. There are three concepts `event`, `listener` and
`publisher` for the [event driven](https://github.com/oss-academy/article/blob/main/event-driven.md) mechanism.

### Event

There are two mechanism in Spring to define event.

**POJO**

```java
public class SampleEvent {
}
```

**Extend from `ApplicationEvent`**

```java

import org.springframework.context.ApplicationEvent;

public class SampleEvent extends ApplicationEvent {

  public MessageEvent(Object source) {
    super(source);
  }
}
```

### Listener

There are two method to implement a listener, annotation driven and programmatically. Also, it is possible to have async
listener with using `@Async`.

#### Annotation Driven

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

#### Programmatically

```java
import org.springframework.context.ApplicationListener;

@Component
class SampleListener implements ApplicationListener<SampleEvent> {

  @Override
  public void onApplicationEvent(SampleEvent event) {
    // handle UserCreatedEvent
  }
}
```

### Publisher

```java
ApplicationEventPublisher publisher;
publisher.publishEvent(event);
```

## Prerequisites

* Java 17
* Maven 3

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