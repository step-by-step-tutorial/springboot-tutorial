# <p style="text-align: center;"> Integration of Spring Boot And Apache Kafka Messaging</p>

<p style="text-align: justify;">
This tutorial shows how to integrate Apache Kafka in Spring Boot application.
</p>

## <p style="text-align: center;"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [Kafka](#kafka)

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
curl -X GET http://localhost:8080/api/v1/health-check
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
kubectl port-forward service/application 8080:8080 -n dev
```

```shell
kubectl port-forward service/kafdrop-service 9000:9000 -n dev
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
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
* Application: [http://localhost:8080](http://localhost:8080)

## Kafka

<p style="text-align: justify;">

Apache Kafka is an event streaming platform. It follows a publish / subscribe pattern around streams of events. The
Kafka
supports only Topic. There are a few concepts such as event, stream of events, producer and consumer. Topics can support
many producers and many consumers it means there is many-to-many relation between producer and consumer based on Topic.

For more information see the [https://kafka.apache.org](https://kafka.apache.org).

</p>

<p style="text-align: center;">

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/messaging-apache-kafka/doc/kafka-solution.gif" width="426" height="240">

</p>

##

**<p style="text-align: center;"> [Top](#integration-of-spring-boot-and-apache-kafka) </p>**