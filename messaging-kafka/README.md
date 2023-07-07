# <p align="center">Kafka MQ</p>

<p>
Apache Kafka MQ is a message queue, for more information see the [https://kafka.apache.org/](https://kafka.apache.org/).
</p>

## Install Active MQ Artemis on Docker

Execute the `docker compose  up -d` command to install Kafka on docker, also, you can use the following commands.

**Help**

```shell
# check if docker was install on your machine
docker --version
docker-compose --version
docker-machine --version

# remove current container and image
docker rm kafka --force
docker rm zookeeper --force
docker rm kafdrop --force
docker image rm confluentinc/cp-zookeeper:latest
docker image rm confluentinc/cp-kafka:latest
docker image rm obsidiandynamics/kafdrop:latest

# install and deploy artemis
docker compose --file docker-compose.yml --project-name kafka up -d```
```

**Docker Compose file**

```yaml
version: '2'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    container_name: zookeeper
    hostname: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:latest
    container_name: kafka
    hostname: kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    volumes:
      - "./docker/kafka:/var/lib/kafka/data"
  kafdrop:
    image: obsidiandynamics/kafdrop:latest
    container_name: kafdrop
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKERCONNECT: kafka:9092
      JVM_OPTS: "-Xms32M -Xmx64M"
```

## How To Config Spring Boot

### Dependency

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Spring Boot Properties

```yaml
spring:
  kafka:
    topic:
      name: ${KAFKA_TOPIC_NAME:main-topic}
    consumer:
      group-id: ${KAFKA_GROUP_ID:main-group}
    bootstrap-servers: ${KAFKA_URL:localhost:9092}

---
spring:
  profiles:
    active: embedded
  kafka:
    topic:
      name: embedded-topic
    consumer:
      auto-offset-reset: earliest
      group-id: embedded-group
    listener:
      ack-mode: manual
      client-id: embedded-client-id
      concurrency: 10
      type: batch
    admin:
      client-id: admin-embedded-client-id

```


### Web Console (kafdrop)

Open [http://localhost:9000/](http://localhost:9000/) in the browser.

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

**<p align="center"> [Top](#kafka-mq) </p>**