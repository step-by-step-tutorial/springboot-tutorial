# <p text-align="center">Rabbit MQ</p>

<p>
Rabbit Mq is a message queue for more information see the [https://www.rabbitmq.com/](https://www.rabbitmq.com/).
</p>

## Install Active MQ Artemis on Docker

Execute the `docker compose  up -d` command to install RabbitMQ on docker, also, you can use the following commands.

**Help**
```shell
# check if docker was install on your machine
docker --version
docker-compose --version
docker-machine --version

# remove current container and image
docker rm rabbitmq --force
docker image rm rabbitmq:management

# install and deploy artemis
docker compose --file docker-compose.yml --project-name rabbitmq up -d
```

**Docker Compose file**
```yaml
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq:management
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      - RABBITMQ_DEFAULT_USER=guest
      - RABBITMQ_DEFAULT_PASS=guest
```

## How To Config Spring Boot

### Dependencies

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

```

### Spring Boot Properties

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

```

### How to Active RabbitMq

```java

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {
}

```

### Web Console

Open [http://localhost:15672/](http://localhost:15672/) in the browser.

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

**<p text-align="center"> [Top](#rabbit-mq) </p>**