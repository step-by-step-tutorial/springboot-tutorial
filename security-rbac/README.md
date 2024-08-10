# <p align="center">Role-Based Access Control</p>

<p align="justify">

This tutorial is about integration of Spring Boot and RBAC.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [RBAC](#rbac)
* [RBAC Use Cases](#rbac-use-cases)
* [Install Application on Docker](#install-app-on-docker)
* [Install Application on Kubernetes](#install-app-on-kubernetes)
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

```yaml
URL: http://localhost:8080/hello
ADMIN_URL: http://localhost:8080/admin/hello
USER_URL: http://localhost:8080/user/hello
```

## RBAC

<p align="justify">

For more information about RBAC see the []().

</p>

## RBAC Use Cases

## Install Application on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
```

### Apply Docker Compose

Execute the following command to install Application.

```shell
docker compose --file ./docker-compose.yml --project-name securityrbac up --build -d
```

## Install Application on Kubernetes

Create the following files for installing Application.

### Kube Files

[app-deployment.yml](/kube/app-deployment.yml)

```yaml
#deployment.yml
```

[app-service.yml](/kube/app-service.yml)

```yaml
#service.yml
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/app-deployment.yml
kubectl apply -f ./kube/app-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Application from localhost through the web browser use the following command and dashboard of
Application is available on [http://localhost:port](http://localhost:port) URL.

</p>

```shell
kubectl port-forward service/securityrbac port:port
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

**<p align="center"> [Top](#attribute-based-access-control) </p>**