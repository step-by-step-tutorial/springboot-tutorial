# <p align="center">Integration of Spring Boot And Apache Kafka Messaging</p>

<p align="justify">
This tutorial shows how to integrate Apache Kafka in Spring Boot application.
</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Apache Kafka](#apache-kafka)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

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

### Run

```shell
mvn  spring-boot:start
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

Check application console log.

### Stop

```shell
mvn  spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
```

## Apache Kafka

<p align="justify">

Apache Kafka is an event streaming platform. It follows publish/subscriber pattern around streams of events. The Kafka
supports only Topic. There are a few concepts such as event, stream of events, producer and consumer. Topics can support
many producer and many consumer it means there is many-to-many relation between producer and consumer based on Topic.

For more information see the [https://kafka.apache.org](https://kafka.apache.org).

</p>

<p align="center">

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/messaging-apache-kafka/doc/kafka-solution.gif" width="426" height="240">

</p>

### Apache Use Cases

* Messaging
* Website Activity Tracking
* Metrics
* Log Aggregation
* Stream Processing
* Event Sourcing
* Commit Log

## Dockerized

### Docker Compose

Create a file named docker-compose.yml with the following configuration. It includes Zookeeper, Kafka and Kafdrop
services.

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
name: dev-env
services:
  zookeeper:
    image: docker.io/bitnami/zookeeper
    container_name: zookeeper
    hostname: zookeeper
    restart: always
    ports:
      - "2181:2181"
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    image: docker.io/bitnami/kafka
    container_name: kafka
    hostname: kafka
    restart: always
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper
    environment:
      KAFKA_CFG_BROKER_ID: 1
      ALLOW_PLAINTEXT_LISTENER: yes
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_LISTENERS: LOCALHOST://:9092,CONTAINER://:9093
      KAFKA_CFG_ADVERTISED_LISTENERS: LOCALHOST://localhost:9092,CONTAINER://kafka:9093
      KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: LOCALHOST:PLAINTEXT,CONTAINER:PLAINTEXT
      KAFKA_CFG_INTER_BROKER_LISTENER_NAME: LOCALHOST
  kafdrop:
    image: obsidiandynamics/kafdrop:latest
    container_name: kafdrop
    hostname: kafdrop
    restart: always
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKERCONNECT: kafka:9093
      JVM_OPTS: "-Xms32M -Xmx64M"
```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker compose --file docker-compose.yml --project-name dev-env up --build -d
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

Check application console log.

### Down

```shell
docker compose --file docker-compose.yml --project-name dev-env down
```

```shell
docker image rm samanalishiri/application:latest
```
## Kubernetes

### Kube Files

[zookeeper.yml](/kube/zookeeper.yml)

```yaml
#zookeeper.yml
```

[kafka.yml](/kube/kafka.yml)

```yaml
#kafka-deployment.yml
```

[kafdrop.yml](/kube/kafdrop.yml)

```yaml
#kafdrop.yml

```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker build -t samanalishiri/application:latest .  --no-cache
```

```shell
kubectl apply -f ./kube/kafka.yml
```

```shell
kubectl apply -f ./kube/application.yml
```

### Check Status

```shell
kubectl get all
```

### E2eTest

```shell
kubectl port-forward service/application 8080:8080
```

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

Check application console log.

### Port Forwarding

```shell
kubectl port-forward service/application 8080:8080
```

```shell
kubectl port-forward service/kafdrop-service 9000:9000
```

### Down

```shell
kubectl delete all --all
```

```shell
docker image rm samanalishiri/application:latest
```

## UI

Kafdrop: [http://localhost:9000](http://localhost:9000)

## How To Set up Spring Boot

### Dependencies

```xml

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### Application Properties

```yaml
spring:
  kafka:
    topic:
      name: ${KAFKA_TOPIC_NAME:main-topic}
    consumer:
      group-id: ${KAFKA_GROUP_ID:main-group}
    bootstrap-servers: ${KAFKA_URL:localhost:9092}
```

## How To Set up Spring Boot Test

The embedded Kafka is used for unit tests.

### Dependencies

```xml

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Application Properties

```yaml
# application-embedded-kafka.yml
spring:
  kafka:
    topic:
      name: embedded-topic
    consumer:
      group-id: embedded-group
    bootstrap-servers: ${KAFKA_URL:localhost:9092}
```

### Java Config

```java

@SpringBootTest
@ActiveProfiles({"embedded-kafka"})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TestClass {

}
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-kafka) </p>**