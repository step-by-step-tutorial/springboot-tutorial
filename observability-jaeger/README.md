# <p align="center">Integration of Spring Boot And Jaeger</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Jaeger.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Jaeger](#jaeger)
* [Jaeger Use Cases](#jaeger-use-cases)
* [Install Jaeger on Docker](#install-jaeger-on-docker)
* [Install Jaeger on Kubernetes](#install-jaeger-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [License](#license)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
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

```shell
curl http://localhost:8080/api/v1/application/status
```

## Jaeger

<p align="justify">

For more information about Jaeger see the [https://www.jaegertracing.io](https://www.jaegertracing.io).

</p>

## Jaeger Use Cases

## Install Jaeger on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: '3.9'
services:
  jaeger:
    container_name: jaeger
    hostname: jaeger
    image: jaegertracing/all-in-one:latest
    ports:
      - "16686:16686"
      - "4318:4318"
  observabilityjaeger:
    image: samanalishiri/observabilityjaeger:latest
    build:
      context: .
      dockerfile: ./Dockerfile
    container_name: observabilityjaeger
    hostname: observabilityjaeger
    restart: always
    ports:
      - "8080:8080"
    environment:
      APP_HOST: "0.0.0.0"
      APP_PORT: "8080"
      TRACING_HOST: http://jaeger:4318/v1/traces
```

### Apply Docker Compose

Execute the following command to install Jaeger.

```shell
docker compose --file ./docker-compose.yml --project-name observabilityjaeger up --build -d
```

## Install Jaeger on Kubernetes

Create the following files for installing Jaeger.

### Kube Files

[jaeger-deployment.yml](/kube/jaeger-deployment.yml)

```yaml
#deployment.yml
```

[jaeger-service.yml](/kube/jaeger-service.yml)

```yaml
#service.yml
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/jaeger-deployment.yml
kubectl apply -f ./kube/jaeger-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Jaeger from localhost through the web browser use the following command and dashboard of
Jaeger is available on [http://localhost:16686](http://localhost:16686) URL.

</p>

```shell
kubectl port-forward service/jaeger 16686:16686
```

## How To Set up Spring Boot

### Dependencies

```xml
```

### Application Properties

```yaml
```

## How To Set up Spring Boot Test

### Dependencies

```xml
```

### Application Properties

```yaml
```

## License

## Appendix

### Makefile

```shell
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run
	
docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name tools-name up --build -d

docker-remove-container:
	docker rm tools-name --force

docker-remove-image:
	docker image rm image-name

kube-deploy:
	kubectl apply -f ./kube/tools-name-deployment.yml
	kubectl apply -f ./kube/tools-name-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-db:
	kubectl port-forward service/tools-name port:port
```

##

**<p align="center"> [Top](#title) </p>**