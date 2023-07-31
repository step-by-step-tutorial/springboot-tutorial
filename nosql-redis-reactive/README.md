# <p align="center">NoSQL Reactive Redis</p>

<p align="justify">

This tutorial is included [Redis](https://redis.io/) configuration for test and none test environment. This tutorial
uses [Lettuce](https://lettuce.io/) to create connection factories for making connection to the Redis.

</p>

## Install Redis on Docker

```yaml
version: "3.8"

services:
  redis:
    image: redis:latest
    container_name: redis
    hostname: redis
    restart: always
    ports:
      - "6379:6379"
  redisinsight:
    image: redislabs/redisinsight:latest
    container_name: redisinsight
    hostname: redisinsight
    restart: always
    volumes:
      - "./docker/redislabs:/db"
    ports:
      - "8001:8001"

```

Also, there is another alternative for the Redisinsight to access redis named Commander.

```yaml
  commander:
    image: rediscommander/redis-commander:latest
    container_name: commander
    hostname: commander
    restart: always
    environment:
      - REDIS_HOSTS=local:redis:6379
    ports:
      - "8081:8081"
```

In order to connect to redis via Web

* by commander use the [http://localhost:8081/](http://localhost:8081/)
* by redisinsight use [http://host-IP:8001/](http://host-IP:8001/) then select the `I already have a database` and
  continue with the following properties.
  ```yaml
  host: IP of the host machine (only IP and not localhost)
  port: 6379
  database: just a name
  ```
  
## Install Redis on Kubernetes

## How To Config Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>
    <dependency>
        <groupId>io.lettuce</groupId>
        <artifactId>lettuce-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```

### Test Dependency

```xml

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
      repositories:
        enabled: false
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

**<p align="center"> [Top](#nosql-reactive-redis) </p>**