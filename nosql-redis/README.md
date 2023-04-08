# <p align="center">NoSQL Redis</p>

<p align="justify">

This tutorial is included [Redis](https://redis.io/) configuration for test and none test environment. This tutorial 
uses two strategy to create connection factories for making connection to the Redis.

* [Jedis](https://redis.io/docs/clients/java/)
* [Lettuce](https://lettuce.io/)

</p>

## Install Redis on Docker

Execute the `docker compose  up -d` command to install Redis.

```yaml
version: "3.8"

services:
  redis:
    container_name: redis
    hostname: redis
    image: redis:latest
    ports:
      - "6379:6379"

```

## How To Config Spring Boot

### Dependency

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
</dependency>
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>

<dependency>
    <groupId>com.github.kstyrc</groupId>
    <artifactId>embedded-redis</artifactId>
    <version>0.6</version>
    <scope>test</scope>
</dependency>

```

### Spring Boot Properties

```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}

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
mvn  test
```

## Run

```bash
mvn  spring-boot:run
```

**<p align="center"> [Top](#nosql-redis) </p>**