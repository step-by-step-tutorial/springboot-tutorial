# <p align="center">Event Processor</p>

This sample is about working event and event listener in Spring Boot. There are three concepts `event`, `listener` and
`publish` for the [event driven](https://github.com/oss-academy/article/blob/main/event-driven/README.md) mechanism.

## Event

There are two mechanism in Spring to define an event. One solution is, create a java bean and another one is, create a
class extended from `ApplicationEvent`.

**Java Bean**

```java
public record EventModel(String text) {
}

```

**ApplicationEvent**

```java

import org.springframework.context.ApplicationEvent;

public class EventModel extends ApplicationEvent {

    public EventModel(Object source) {
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
public class EventHandler {

    @EventListener
    void onEvent(final EventModel event) {
    }

}
```

```java
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    @Async
    @EventListener
    void onEvent(final EventModel event) {
    }
}
```

### Programmatically

```java
import org.springframework.context.ApplicationListener;

@Component
class EventHandler implements ApplicationListener<EventModel> {

    @Override
    public void onApplicationEvent(EventModel event) {
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
public class EventPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publish(EventModel event) {
        publisher.publishEvent(event);
    }

}
```

## Dependencies

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
</dependency>
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

**<p align="center"> [Top](#event-processor) </p>**