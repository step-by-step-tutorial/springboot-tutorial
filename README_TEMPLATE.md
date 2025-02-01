# <p align="center">Title</p>

<p align="justify">

This tutorial is about integration of Spring Boot and TOOL_NAME.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [TOOL_NAME](#tool_name)
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
```

### Stop

```shell
mvn  spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
```

## TOOL_NAME

<p align="justify">

For more information about ELK see the [https://www.tool_name](https://www.tool_name).

</p>

### Use Cases

## Dockerized

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
name: dev-env
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
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev-env down
```

## Kubernetes

### Kube Files

[application.yml](/kube/application.yml)

```yaml

```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker build -t samanalishiri/application:latest .
```

```shell
kubectl apply -f ./kube/application.yml
```

### Check Status

```shell
kubectl get all
```

### Port-Forwarding

```shell
kubectl port-forward service/application 8080:8080
```

### E2eTest

```shell
kubectl port-forward service/application 8080:8080
```

```shell
```

### Down

```shell
kubectl delete all --all
```

```shell
kubectl delete secrets ???
```

```shell
kubectl delete configMap ???
```

```shell
docker image rm samanalishiri/application:latest
```

## UI

* Application: [http://localhost:8080](http://localhost:8080)

##

**<p align="center"> [Top](#title) </p>**