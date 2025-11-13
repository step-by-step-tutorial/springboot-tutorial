# <p align="center"> Integration of Spring Boot And Apache Pulsar</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [Apache Pulsar](#apache-pulsar)

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

### Port Forwarding

```shell
kubectl port-forward service/application 8080:8080 -n dev
```

```shell
kubectl port-forward service/broker 8081:8081 -n dev
```

```shell
kubectl port-forward service/dashboard 9527:9527 -n dev
```

```shell
kubectl port-forward service/dashboard 7750:7750 -n dev
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

* Application: [http://localhost:8080](http://localhost:8080)
* Pulsar Manager: [http://localhost:7750](http://localhost:7750)
* Pulsar Admin UI: [http://localhost:9527](http://localhost:9527)

## Apache Pulsar

### Manager

**Get CSRF Token from Apache Pulsar**

```shell
CSRF_TOKEN=$(curl http://localhost:7750/pulsar-manager/csrf-token)
echo $CSRF_TOKEN
```

**Create User**

```shell
CSRF_TOKEN=$(curl http://localhost:7750/pulsar-manager/csrf-token)
curl -X PUT http://localhost:7750/pulsar-manager/users/superuser \
   -H 'X-XSRF-TOKEN: $CSRF_TOKEN' \
   -H 'Cookie: XSRF-TOKEN=$CSRF_TOKEN;' \
   -H "Content-Type: application/json" \
   -d '{"name": "admin", "password": "password", "description": "administrator", "email": "admin@email.com"}'
```

**Pulsar Dashboard**

Link: [http://localhost:9527](http://localhost:9527)

```yaml
environment-name: dev
broker-url:
  docker: http://broker:8080
  kubernetes: http://broker:8081
bookie-url: http://bookie:3181
```

### API

**Create Topic**

```shell
curl -X PUT http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitions -H 'Content-Type: application/json' -d "4"
```

**Update Topic**

```shell
curl -X POST http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitions -H 'Content-Type: application/json' -d "5"
```

**Get topic Info**

```shell
curl -X GET http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitioned-internalStats | jq
```

**Get namespace Info**

```shell
curl -X GET http://localhost:8081/admin/v2/persistent/public/default | jq
```

**Delete Topic**

```shell
curl -X DELETE http://localhost:8081/admin/v2/persistent/public/default/test-topic/partitions
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-pulsar) </p>**
