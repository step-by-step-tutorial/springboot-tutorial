# <p align="center">Kafka MQ</p>

<p align="justify">

Apache Kafka MQ is a message queue, for more information see the [https://kafka.apache.org/](https://kafka.apache.org/).

</p>

## Install Kafka on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

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
      - "./target/kafka:/var/lib/kafka/data"
  kafdrop:
    image: obsidiandynamics/kafdrop:latest
    container_name: kafdrop
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKERCONNECT: kafka:9092
      JVM_OPTS: "-Xms32M -Xmx64M"
```

Execute the `docker compose  up -d` command to install Zookeeper, Kafka and Kafdrop.

```shell
# full command
docker compose --file ./docker-compose.yml --project-name kafka up --build -d

```

### Kafdrop

Open [http://localhost:9000/](http://localhost:9000/) in the browser.

## Install Kafka on Kubernetes

Create the following files for installing Kafka.

### Kafka

**zookeeper-deployment.yml**

```yaml
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
          image: confluentinc/cp-zookeeper:latest
          ports:
            - containerPort: 2181
          env:
            - name: ZOOKEEPER_CLIENT_PORT
              value: "2181"
            - name: ZOOKEEPER_TICK_TIME
              value: "2000"
```

**zookeeper-service.yml**

```yaml
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

**kafka-deployment.yml**

```yaml
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
          image: confluentinc/cp-kafka:latest
          ports:
            - containerPort: 9092
          env:
            - name: KAFKA_BROKER_ID
              value: "1"
            - name: KAFKA_ZOOKEEPER_CONNECT
              value: 'zookeeper-service:2181'
            - name: KAFKA_LISTENER_SECURITY_PROTOCOL_MAP
              value: PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT
            - name: KAFKA_ADVERTISED_LISTENERS
              value: PLAINTEXT://:29092,PLAINTEXT_INTERNAL://kafka-service:9092
            - name: KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR
              value: "1"
            - name: KAFKA_TRANSACTION_STATE_LOG_MIN_ISR
              value: "1"
            - name: KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR
              value: "1"

```

**kafka-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: kafka-service
spec:
  selector:
    app: kafka
  ports:
    - port: 9092
      targetPort: 9092
```

### Kafdrop

**kafdrop-deployment.yml**

```yaml
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
              value: kafka-service:9092
            - name: JVM_OPTS
              value: "-Xms32M -Xmx128M"

```

**kafdrop-service.yml**

```yaml
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

### Apply Configuration Files

Execute the following commands to install the tools on Kubernetes.

```shell
# ======================================================================================================================
# Zookeeper
# ======================================================================================================================
kubectl apply -f ./kube/zookeeper-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment zookeeper -n default

kubectl apply -f ./kube/zookeeper-service.yml
# kubectl get service -n default
# kubectl describe service zookeeper-service -n default

# ======================================================================================================================
# Kafka
# ======================================================================================================================
kubectl apply -f ./kube/kafka-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment kafka -n default

kubectl apply -f ./kube/kafka-service.yml
# kubectl get service -n default
# kubectl describe service kafka-service -n default

# ======================================================================================================================
# Kafdrop
# ======================================================================================================================
kubectl apply -f ./kube/kafdrop-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment kafdrop -n default

kubectl apply -f ./kube/kafdrop-service.yml
# kubectl get service -n default
# kubectl describe service kafdrop -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all
```

<p align="justify">

In order to connect to Kafdrop from localhost through the web browser use the following command and dashboard of Kafdrop
is available on [http://localhost:9000](http://localhost:9000) URL.

</p>

```shell
# kafdrop
# http://localhost:9000
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

## How To Set up Test

The embedded Kafka used for unit tests.

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

### Java Config for Test

```java
@SpringBootTest
@ActiveProfiles({"embedded-kafka"})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TestClass {
    
}
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

##

**<p align="center"> [Top](#kafka-mq) </p>**