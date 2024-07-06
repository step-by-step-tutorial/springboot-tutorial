# <p align="center">Title</p>

<p align="justify">

introduction

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [TOOLS_NAME](#tools_name)
* [TOOLS_NAME use cases](#tools_name-use-cases)
* [Install TOOLS_NAME on Docker](#install-tools_name-on-docker)
* [Install TOOLS_NAME on Kubernetes](#install-tools_name-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Prerequisites](#prerequisites)
* [Pipeline](#pipeline )

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

## TOOLS_NAME

## TOOLS_NAME Use Cases

## Install TOOLS_NAME on Docker

### Docker Compose File

```yaml
#docker-compose.yml
```

### Apply Docker Compose File

```shell
docker compose --file ./docker-compose.yml --project-name tools_name up --build -d
```

## Install TOOLS_NAME on Kubernetes

### Kube Files

```yaml
#deployment.yml
```

```yaml
#service.yml
```

### Apply Kube Files

```shell
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

### Make File

```shell
docker-deploy:
	docker compose --file docker-compose.yml --project-name tools-name up -d

docker-rebuild-deploy:
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