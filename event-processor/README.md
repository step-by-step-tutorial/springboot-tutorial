# <p text-align="center">Event Processor</p>

<p text-align="justify">

This sample is about working event and event listener in Spring Boot. There are three concepts `event`, `listener` and
`publish` for the event driven mechanism.

</p>

## Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Implementation

### Event

There are two mechanisms in the Spring Boot to define an event. One solution is, create a java bean and another one is,
create a class extended from `ApplicationEvent`.

#### Java Bean

```java
public record EventModel(String text) {
}

```

#### ApplicationEvent

```java

import org.springframework.context.ApplicationEvent;

public class EventModel extends ApplicationEvent {

    public EventModel(Object source) {
        super(source);
    }
}
```

### Listener

<p text-align="justify">

There are two methods to implement a listener, annotation driven and programmatically. Also, it is possible to have
async listener with using `@Async`.

</p>

#### Annotation Driven

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

#### Programmatically

```java

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
class EventHandler implements ApplicationListener<EventModel> {

    @Override
    public void onApplicationEvent(@NonNull EventModel event) {
    }
}
```

### Publisher

Publisher is used to publish/dispatch events and, the Spring Boot has a class named `ApplicationEventPublisher` to do
that.

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

**<p text-align="center"> [Top](#event-processor) </p>**