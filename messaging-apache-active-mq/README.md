# <p align="center">Apache Active MQ</p>

<p>

There are two distribution of Apache Active MQ, [Classic](https://activemq.apache.org/components/classic/) and the other 
is [Artemis](https://activemq.apache.org/components/artemis/). This tutorial used Artemis distribution.

</p>


## Install Active MQ Artemis on Docker

Execute the `docker compose  up -d` command to install Artemis.

```yaml

```

## How To Config Spring Boot

### Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-artemis</artifactId>
</dependency>
```

### Spring Boot Properties

```yaml
spring:
  artemis:
    mode: native
    host: ${ACTIVE_MQ_HOST:localhost}
    port: ${ACTIVE_MQ_PORT:61616}
    user: ${ACTIVE_MQ_USER:admin}
    password: ${ACTIVE_MQ_PASS:admin}
```
## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)

## Build

```bash
mvn clean package -DskipTests=true 
```

## Test

```bash
mvn test
```

## Run

```bash
mvn  spring-boot:run
```

**<p align="center"> [Top](#apache-active-mq) </p>**