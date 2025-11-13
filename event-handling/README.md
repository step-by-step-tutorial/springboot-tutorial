# <p align="center">Event Handling In Spring Boot </p>

<p style="text-align: justify;">

This sample is about working with event and event listener in Spring Boot. There are three concepts `event`, `listener`
and `publish` for the event driven mechanism. This tutorial used builtin event handling solution of Spring Boot.

</p>

## <p align="center">Table of Content </p>

* [Getting Started](#getting-started)
* [spring Boot](#spring-boot)
    * [Event](#event)
    * [Listener](#listener)
    * [Publisher](#publisher)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)

### Build

```shell
mvn validate clean compile 
```

### Test

```shell
mvn test
```

### Package

```shell
mvn package -DskipTests=true
```

## Spring Boot

### Event

There are two mechanisms in the Spring Boot to define an event. One solution is, create a java bean and another one is,
create a class extended from `ApplicationEvent`.

#### _Java Bean_

```java
public record EventModel(String text) {
}
```

#### _ApplicationEvent_

```java
import org.springframework.context.ApplicationEvent;

public class EventModel extends ApplicationEvent {

    public EventModel(Object source) {
        super(source);
    }
}
```

### Listener

<p style="text-align: justify;">

There are two methods to implement a listener, annotation driven and programmatically. Also, it is possible to have
async listener with using `@Async`.

</p>

#### _Annotation Driven_

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

#### _Programmatically_

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

##

**<p align="center">[Top](#event-handling-in-spring-boot) </p>**
