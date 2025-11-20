# <p align="center"> Integration of Spring Boot And Apache Kafka Streaming</p>

<p style="text-align: justify;">
This tutorial shows how to integrate Apache Kafka in Spring Boot application.
</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [Apache Kafka](#apache-kafka)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

### Build

```shell
mvn validate clean compile -DskipTests=true
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
```

### Stop

```shell
mvn  spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
docker volume prune -f
```

## Dockerized

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker compose --file docker-compose.yml --project-name dev up --build -d
```

### E2eTest

```shell
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev down
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## Kubernetes

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker build -t samanalishiri/application:latest .  --no-cache
kubectl apply -f kube-dev.yml
```

### Check Status

```shell
kubectl get all -n dev
```

### Port Forwarding

```shell
kubectl port-forward service/kafdrop-service 9000:9000 -n dev
```

### E2eTest

```shell
```

### Down

```shell
kubectl delete all --all -n dev
kubectl delete secrets dev-credentials -n dev
kubectl delete configMap dev-config -n dev
kubectl delete persistentvolumeclaim database-pvc -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* Kafdrop: [http://localhost:9000](http://localhost:9000)

## Apache Kafka

<p style="text-align: justify;">

Apache Kafka is an event streaming platform. It follows the publishing/subscriber pattern around streams of events. The
Kafka supports only Topic. There are a few concepts such as event, stream of events, producer and consumer. Topics can
support many producers and many consumers it means there is many-to-many relation between producer and consumer based on
Topic.

For more information see the [https://kafka.apache.org](https://kafka.apache.org).

</p>

### Apache Kafka Streaming Concepts

#### Stream

It is a sequence of immutable key-value data.

#### Stream Processing Topology

It is a data flow of data in a key-value format that includes entry point/consuming data, processing data and out
point/producing data. These steps (processors) are in directed acyclic graph (DAG) relation. Indeed, it is a logical
abstraction of your stream processing implementation.

#### Source Processor

It is a processor that consumes data from the input Topics.

#### Sink Processor

It is a processor that produces data for the output Topics.

<p align="center">

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/streaming-apache-kafka/doc/kafka-streaming-processor-topology.png" width="426" height="240">

</p>

#### Streams and Tables

#### Aggregations

An aggregation takes an input stream or table then yields a new table by combining multiple input records into a single
output record.

```text
KStream/KTable  -- aggregation -->  KTable.
```

### Apache Use Cases

* Messaging
* Website Activity Tracking
* Metrics
* Log Aggregation
* Stream Processing
* Event Sourcing
* Commit Log

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-kafka) </p>**
