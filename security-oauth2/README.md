# <p align="center">Implement OAuth 2.0 Server and Client</p>

<p align="justify">

This tutorial is about implementing OAuth 2.0 Server and Client with Springboot framework.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [OAuth 2.0](#tools_name)
* [OAuth 2.0 Use Cases](#tools_name-use-cases)
* [Install OAuth 2.0 on Docker](#install-tools_name-on-docker)
* [Install OAuth 2.0 on Kubernetes](#install-tools_name-on-kubernetes)
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

#### Generate Certificate

```bash
openssl genpkey -algorithm RSA -out private-key.pem
openssl rsa -pubout -in private-key.pem -out public-key.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -in private-key.pem -out private-key.pem -nocrypt
```

#### Build

```bash
# server
mvn -f security-oauth2-server/pom.xml clean package -DskipTests=true
```

```bash
# client
mvn -f security-oauth2-client/pom.xml clean package -DskipTests=true
```

#### Test

```bash
# server
mvn -f security-oauth2-server/pom.xml test
```

```shell
# client
mvn -f security-oauth2-client/pom.xml test
```

#### Run

```bash
# server
mvn -f security-oauth2-server/pom.xml spring-boot:run
```

#### Register a Client

```shell
curl -v -X POST "http://localhost:8080/api/v1/clients" \
  -u "test:test" \
  -H "Content-Type: application/json" \
  -d '{
        "clientId": "testClient",
        "clientSecret": "password",
        "redirectUri": "http://localhost:8080/login/oauth2/code/testClient",
        "grantTypes": ["authorization_code", "password", "client_credentials", "refresh_token"],
        "scopes": ["read", "write"],
        "accessToken_validity_seconds": 3600,
        "refreshToken_validity_seconds": 1209600
      }'
```

#### Create a Login Session

```shell
curl -v -X POST "http://localhost:8080/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=test&password=test" \
  -c cookies.txt
```

#### Request an Authorization Code

Copy authorization code from console.

```shell
curl -v -X GET "http://localhost:8080/oauth2/authorize?response_type=code&client_id=testClient&scope=read&redirect_uri=http://localhost:8080/login/oauth2/code/testClient" \
  -b cookies.txt \
  -H "Content-Type: application/x-www-form-urlencoded"

```

#### Get a JWT Bearer Token

Use authorization code from last step.
Copy token from console.

```shell
 curl -X POST "http://localhost:8080/oauth2/token" \
  -u "testClient:password" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "code=???" \
  -d "redirect_uri=http://localhost:8080/login/oauth2/code/testClient"
```

#### Request a Protected Resource

Use token from last step.

```shell
# Check health status
curl -v -X GET "http://localhost:8080/api/v1/health" \
  -H "Authorization: Bearer ???"
```

## OAuth 2.0

<p align="justify">

For more information about OAuth 2.0 see the []().

</p>

## OAuth 2.0 Use Cases

## Install Applications on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
```

### Apply Docker Compose

Execute the following command to install Applications.

```shell
docker compose --file ./docker-compose.yml --project-name tools_name up --build -d
```

## Install Applications on Kubernetes

Create the following files for installing Applications.

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

In order to connect to Applications from localhost through the web browser use the following command and dashboard of
Applications is available on:

* Server: [http://securityoauth2server.localhost](http://securityoauth2server.localhost)
* Client: [http://securityoauth2client.localhost](http://securityoauth2client.localhost)

</p>

```shell
kubectl port-forward service/securityoauth2server 8080:8080
```

```shell
kubectl port-forward service/securityoauth2client 8081:8081
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