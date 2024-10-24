# <p align="center">Title</p>

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

```bash
mvn clean package -DskipTests=true
#http://localhost:8080/oauth2/authorize?response_type=code&client_id=test-client&redirect_uri=http://localhost:8080/login/oauth2/code/test-client&scope=read
#openssl genpkey -algorithm RSA -out private-key.pem
#openssl rsa -pubout -in private-key.pem -out public-key.pem
#openssl pkcs8 -topk8 -inform PEM -outform PEM -in private-key.pem -out private-key.pem -nocrypt
```

#### Test

```bash
mvn test
http://localhost:9000/oauth2/authorization/test-client

  curl -X POST "http://localhost:8080/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -u "test-client:test-secret" \
  -d "grant_type=authorization_code" \
  -d "code=apT7bwZzFxCX4-ESMEFOt2Jn6E2tduEc512Hungfgtcar_7w2sGnNb0mVIo--U3AhOMyMy1X1p2tYahWrFHvPn9IeOswd56nTRd0ajdesSozacZbNXktHH0jFVO9HReT" \
  -d "redirect_uri=http://localhost:9000/login/oauth2/code/test-client"


```

#### Run

```bash
mvn  spring-boot:run
```

## TOOLS_NAME

<p align="justify">

For more information about TOOLS_NAME see the []().

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
docker compose --file ./docker-compose.yml --project-name tools_name up --build -d
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