# <p align="center">Title</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )

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
```

## Dockerized

### Docker Compose

[Docker Compose](docker-compose.yml)

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker compose --file ./docker-compose.yml --project-name dev-env up --build -d
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev-env down
```

## Kubernetes

### Kube Files

[Cloud Native Development Environment](kube-dev-env.yml)

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker build -t samanalishiri/application:latest .  --no-cache
```

```shell
kubectl apply -f kube-dev-env.yml
```

### Check Status

```shell
kubectl get all -n dev-env
```

### Port Forwarding

```shell
kubectl port-forward service/application 8080:8080 -n dev-env
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
kubectl delete all --all -n dev-env
kubectl delete secrets dev-secrets -n dev-env
kubectl delete configMap dev-config -n dev-env
docker image rm samanalishiri/application:latest
```

## UI

* Application: [http://localhost:8080](http://localhost:8080)


##

**<p align="center"> [Top](#title) </p>**