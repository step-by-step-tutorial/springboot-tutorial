# <p align="center">Integration of Spring Boot And Apache Kafka Messaging</p>

<p align="justify">
This tutorial shows how to integrate Apache Kafka in Spring Boot application.
</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Apache Kafka](#apache-kafka)
* [Apache Use Cases](#apache-use-cases)
* [Install Kafka on Docker](#install-kafka-on-docker)
* [Install Kafka on Kubernetes](#install-kafka-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Prerequisites](#prerequisites)
* [Pipeline](#pipeline )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Kafka](https://kafka.apache.org)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

### Pipeline

#### Build

```bash
mvn clean package -DskipTests=true 
```

#### Test

```bash
mvn test
```

#### Run

```bash
mvn  spring-boot:run
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

## Apache Use Cases

* Messaging
* Website Activity Tracking
* Metrics
* Log Aggregation
* Stream Processing
* Event Sourcing
* Commit Log

## Install Kafka on Docker

### Docker Compose

Create a file named docker-compose.yml with the following configuration. It includes Zookeeper, Kafka and Kafdrop
services.

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: '3.8'
services:
  zookeeper:
    image: docker.io/bitnami/zookeeper
    container_name: zookeeper
    hostname: zookeeper
    restart: always
    ports:
      - "2181:2181"
    volumes:
      - "./target/zookeeper:/bitnami"
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
    volumes:
      - "./target/kafka:/bitnami"
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

### Apply Docker Compose

Execute the following command to install Apache Kafka.

```shell
docker compose --file ./docker-compose.yml --project-name kafka up --build -d
```

### Web Console

Open [http://localhost:9000](http://localhost:9000) in the browser to access Kafdrop dashboard.

## Install Kafka on Kubernetes

Create the following files for installing Apache Kafka.

### Kube Files

[zookeeper-deployment.yml](/kube/zookeeper-deployment.yml)

```yaml
#zookeeper-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: zookeeper
spec:
  replicas: 1
  selector:
    matchLabels:
      app: zookeeper
  template:
    metadata:
      labels:
        app: zookeeper
    spec:
      containers:
        - name: zookeeper
          image: docker.io/bitnami/zookeeper
          ports:
            - containerPort: 2181
          env:
            - name: ALLOW_ANONYMOUS_LOGIN
              value: "yes"
          volumeMounts:
            - name: zookeeper-data
              mountPath: /bitnami
      volumes:
        - name: zookeeper-data
          emptyDir: { }
```

[zookeeper-service.yml](/kube/zookeeper-service.yml)

```yaml
#zookeeper-service.yml
apiVersion: v1
kind: Service
metadata:
  name: zookeeper-service
spec:
  selector:
    app: zookeeper
  ports:
    - port: 2181
      targetPort: 2181
```

[kafka-deployment.yml](/kube/kafka-deployment.yml)

```yaml
#kafka-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kafka
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kafka
  template:
    metadata:
      labels:
        app: kafka
    spec:
      containers:
        - name: kafka
          image: docker.io/bitnami/kafka
          ports:
            - containerPort: 9092
            - containerPort: 9093
          env:
            - name: KAFKA_CFG_BROKER_ID
              value: "1"
            - name: ALLOW_PLAINTEXT_LISTENER
              value: "yes"
            - name: KAFKA_CFG_ZOOKEEPER_CONNECT
              value: "zookeeper-service:2181"
            - name: KAFKA_CFG_LISTENERS
              value: "LOCALHOST://:9092,CONTAINER://:9093"
            - name: KAFKA_CFG_ADVERTISED_LISTENERS
              value: "LOCALHOST://localhost:9092,CONTAINER://kafka-service:9093"
            - name: KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP
              value: "LOCALHOST:PLAINTEXT,CONTAINER:PLAINTEXT"
            - name: KAFKA_CFG_INTER_BROKER_LISTENER_NAME
              value: "LOCALHOST"
          volumeMounts:
            - name: kafka-data
              mountPath: /bitnami
      volumes:
        - name: kafka-data
          emptyDir: { }
```

[kafka-service.yml](/kube/kafka-service.yml)

```yaml
#kafka-service.yml
apiVersion: v1
kind: Service
metadata:
  name: kafka-service
spec:
  selector:
    app: kafka
  ports:
    - name: localhost
      port: 9092
      targetPort: 9092
    - name: container
      port: 9093
      targetPort: 9093
```

[kafdrop-deployment.yml](/kube/kafdrop-deployment.yml)

```yaml
#kafdrop-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kafdrop
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kafdrop
  template:
    metadata:
      labels:
        app: kafdrop
    spec:
      containers:
        - name: kafdrop
          image: obsidiandynamics/kafdrop:latest
          ports:
            - containerPort: 9000
          env:
            - name: KAFKA_BROKERCONNECT
              value: "kafka-service:9093"
            - name: JVM_OPTS
              value: "-Xms32M -Xmx64M"
```

[kafdrop-service.yml](/kube/kafdrop-service.yml)

```yaml
#kafdrop-service.yml
apiVersion: v1
kind: Service
metadata:
  name: kafdrop-service
spec:
  selector:
    app: kafdrop
  ports:
    - port: 9000
      targetPort: 9000
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/zookeeper-deployment.yml
kubectl apply -f ./kube/zookeeper-service.yml
kubectl apply -f ./kube/kafka-deployment.yml
kubectl apply -f ./kube/kafka-service.yml
kubectl apply -f ./kube/kafdrop-deployment.yml
kubectl apply -f ./kube/kafdrop-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Kafdrop from localhost through the web browser use the following command and dashboard of Kafdrop
is available on [http://localhost:9000](http://localhost:9000) URL.

</p>

```shell
kubectl port-forward service/kafdrop-service 9000:9000
```

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
spring:
  profiles:
    active: embedded-kafka
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

## Appendix

### Makefile

```makefile
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name kafka up --build -d

docker-remove-container:
	docker rm zookeeper --force
	docker rm kafka --force
	docker rm kafdrop --force

docker-remove-image:
	docker image rm docker.io/bitnami/zookeeper
	docker image rm docker.io/bitnami/kafka
	docker image rm obsidiandynamics/kafdrop:latest

kube-deploy:
	kubectl apply -f ./kube/zookeeper-deployment.yml
	kubectl apply -f ./kube/zookeeper-service.yml
	kubectl apply -f ./kube/kafka-deployment.yml
	kubectl apply -f ./kube/kafka-service.yml
	kubectl apply -f ./kube/kafdrop-deployment.yml
	kubectl apply -f ./kube/kafdrop-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-kafkadrop:
	kubectl port-forward service/kafdrop-service 9000:9000
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-kafka) </p>**