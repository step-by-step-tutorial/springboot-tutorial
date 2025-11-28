# <p align="center">NewSQL CockroachDB</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [CockroachDB](#cockroachdb)

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

### Port-Forwarding

```shell
kubectl port-forward service/cockroachdb 26257:26257 -n dev
```

```shell
kubectl port-forward service/cockroachdb 8081:8080 -n dev
```

```shell
kubectl port-forward service/application 8080:8080 -n dev
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

* Cockroach DB: [http://localhost:8081](http://localhost:8081)

## CockroachDB

<p style="text-align: justify;">

For more information about CockroachDB see the [www.cockroachlabs.com/docs](https://www.cockroachlabs.com/docs).

```shell
netstat -ano | findstr :26257
taskkill /PID 12345 /F
```

```postgresql
create database tutorial_db;
show databases;
CREATE USER tutorial_user WITH PASSWORD 'tutorial_password';
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-cockroachdb) </p>**