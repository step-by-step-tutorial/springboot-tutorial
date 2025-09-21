# <p align="center">Integration of Spring Boot And Jaeger</p>

 <p align="justify">

This tutorial is about the integration of Spring Boot and Jaeger.

 </p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [Jaeger](#jaeger)

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
mvn spring-boot:start
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/application/status
```

### Stop

```shell
mvn spring-boot:stop
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
curl -X GET http://localhost:8080/api/v1/application/status
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
kubectl port-forward service/jaeger 16686:16686 -n dev
```

```shell
kubectl port-forward service/jaeger 4318:4318 -n dev
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/application/status
```

### Down

```shell
kubectl delete all --all -n dev
kubectl delete secrets dev-credentials -n dev
kubectl delete configmap dev-config -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator)
* Jaeger UI: [http://localhost:16686](http://localhost:16686)

## Jaeger

* End-to-end distributed tracing with OpenTelemetry (OTLP/HTTP available on :4318).
* Visualize spans, latency, and service dependencies with the Jaeger UI (:16686).
* Common use cases: performance monitoring, troubleshooting, and root cause analysis.

##

**<p align="center"> [Top](#integration-of-spring-boot-and-jaeger) </p>**