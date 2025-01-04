# <p align="center">Integration of Spring Boot And Apache Pulsar</p>

<p align="justify">

This tutorial is about integration of Spring Boot and TOOLS_NAME.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [TOOLS_NAME](#tools_name)
* [TOOLS_NAME Use Cases](#tools_name-use-cases)
* [Install TOOLS_NAME on Docker](#install-tools_name-on-docker)
* [Install TOOLS_NAME on Kubernetes](#install-tools_name-on-kubernetes)
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

```shell
mvn clean package -DskipTests=true 
```

#### Test

```shell
mvn test
```

#### Run

```shell
mvn  spring-boot:run
```

## TOOLS_NAME

<p align="justify">

For more information about TOOLS_NAME see
the [https://pulsar.apache.org/docs/4.0.x/](https://pulsar.apache.org/docs/4.0.x/).

</p>

## TOOLS_NAME Use Cases

## Install TOOLS_NAME on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
```

### Apply Docker Compose

Execute the following command to install TOOLS_NAME.

```shell
docker compose --file ./docker-compose.yml --project-name pulsar up --build -d
```

```shell
curl -X PUT http://localhost:8080/admin/v2/persistent/public/default/test-topic-2/partitions -H 'Content-Type: application/json' -d "4"
```

```shell
curl -X POST http://localhost:8080/admin/v2/persistent/public/default/test-topic-2/partitions -H 'Content-Type: application/json' -d "5"
```

```shell
curl -X GET http://localhost:8080/admin/v2/persistent/public/default/test-topic-2/partitioned-internalStats
```

```shell
curl -X GET http://localhost:8080/admin/v2/persistent/public/default
```

```shell
curl -X DELETE http://localhost:8080/admin/v2/persistent/public/default/test-topic-2/partitions
```

## Install TOOLS_NAME on Kubernetes

Create the following files for installing TOOLS_NAME.

### Kube Files

[tools_name-deployment.yml](/kube/tools_name-deployment.yml)

```yaml
#deployment.yml
```

[tools_name-service.yml](/kube/tools_name-service.yml)

```yaml
#service.yml
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/tools_name-deployment.yml
kubectl apply -f ./kube/tools_name-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to TOOLS_NAME from localhost through the web browser use the following command and dashboard of
TOOLS_NAME is available on [http://localhost:port](http://localhost:port) URL.

</p>

```shell
kubectl port-forward service/tools_name port:port
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

```makefile
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

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-pulsar) </p>**
