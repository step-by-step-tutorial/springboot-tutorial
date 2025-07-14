# <p align="center">Integration of Spring Boot And Grafana</p>

<p align="justify">

This tutorial is about the integration of Spring Boot and Grafana.

</p>

## Grafana Stack

* k6 service running a load test against the above application.
* Tempo service for storing and querying trace information.
* Loki service for storing and querying log information.
* Mimir service for storing and querying metric information.
* Pyroscope service for storing and querying profiling information.
* Beyla services for watching the four-service application and automatically generating signals.
* Grafana service for visualising observability data.
* Grafana Alloy service for receiving traces and producing metrics and logs based on these traces.

Observe the Spring Boot application with three pillars of observability on Grafana:

* Traces with Tempo and OpenTelemetry Instrumentation for Java
* Metrics with Prometheus, Spring Boot Actuator, and Micrometer
* Logs with Loki and Logback

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#install-redis-on-kubernetes)
* [UI](#ui)

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
docker build -t samanalishiri/application:latest . --no-cache
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
kubectl port-forward service/commander 8081:8081 -n dev
```

```shell
kubectl port-forward service/redisinsight 5540:5540 -n dev
```

```shell
kubectl port-forward service/redis 6379:6379 -n dev
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

* Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator)
* Grafana: [http://localhost:3000](http://localhost:3000)
* Prometheus: [http://localhost:9090](http://localhost:9090)
* Tempo: [http://localhost:3200](http://localhost:3200)
* Loki: [http://localhost:3100](http://localhost:3100)
* Pyroscope: [http://localhost:4040](http://localhost:4040)

##

**<p align="center"> [Top](#integration-of-spring-boot-and-grafana) </p>**