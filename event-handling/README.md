# <p align="center">Event Handling</p>

<p align="justify">

This sample is about working with event and event listener in Spring Boot. There are three concepts `event`, `listener`
and `publish` for the event driven mechanism. This tutorial used builtin event handling solution of Spring Boot.

</p>

# Getting Start

## Prerequisites

* [Java 21](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)

## Pipeline

### Build

```bash
mvn clean package -DskipTests=true
```

### Test

```bash
mvn  test
```

### Run

```bash
mvn  spring-boot:run
```

# Basic Concepts

## Dependencies

Add the following dependencies to the POM file of Maven project.

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

<p align="justify">

There are two methods to implement a listener, annotation driven and programmatically. Also, it is possible to have
async listener with using `@Async`.

</p>

#### Annotation Driven

```java
//Sync listener

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    @EventListener
    void onEvent(EventModel event) {
    }

}
```

```java
//Async listener

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    @Async
    @EventListener
    void onEvent(EventModel event) {
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

    private final ApplicationEventPublisher publisher;

    public EventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(EventModel event) {
        publisher.publishEvent(event);
    }

}
```

#

**<p align="center"> [Top](#event-handling) </p>**