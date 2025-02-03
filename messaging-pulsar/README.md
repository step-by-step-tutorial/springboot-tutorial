# <p align="center">Integration of Spring Boot And Apache Pulsar</p>

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
```

```shell
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

```shell
kubectl port-forward service/application 8018:8018 -n dev-env
```

```shell
kubectl port-forward service/application 9527:9527 -n dev-env
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
* Pulsar Admin Dashboard: [http://localhost:8081](http://localhost:8081)
* Pulsar Dashboard: [http://localhost:9527](http://localhost:9527)

## Pulsar

### API

**Get CSRF token from Apache Pulsar**

```shell
CSRF_TOKEN=$(curl http://localhost:7750/pulsar-manager/csrf-token)
echo $CSRF_TOKEN
```

**Create user**

```shell
CSRF_TOKEN=$(curl http://localhost:7750/pulsar-manager/csrf-token)
curl -X PUT http://localhost:7750/pulsar-manager/users/superuser \
   -H 'X-XSRF-TOKEN: $CSRF_TOKEN' \
   -H 'Cookie: XSRF-TOKEN=$CSRF_TOKEN;' \
   -H "Content-Type: application/json" \
   -d '{"name": "admin", "password": "password", "description": "administrator", "email": "admin@email.com"}'
```

**Create topic**

```shell
curl -X PUT http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitions -H 'Content-Type: application/json' -d "4"
```

**Update topic**

```shell
curl -X POST http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitions -H 'Content-Type: application/json' -d "5"
```

**Get topic info**

```shell
curl -X GET http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitioned-internalStats | jq
```

**Get namespace info**

```shell
curl -X GET http://localhost:8081/admin/v2/persistent/public/default | jq
```

**Delete topic**

```shell
curl -X DELETE http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitions
```

### New Environment

Pulsar Dashboard: [http://localhost:9527](http://localhost:9527)

```yaml
environment-name: dev-env
broker-url: http://broker:8080
bookie-url: http://bookie:3181
```


##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-pulsar) </p>**
