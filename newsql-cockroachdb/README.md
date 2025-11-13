# <p align="center">NewSQL CockroachDB</p>

# <p align="center"> Integration of Spring Boot and CockroachDB</p>

<p style="text-align: justify;">

This tutorial is about the integration of Spring Boot and CockroachDB.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [CockroachDB](#cockroachdb)

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
docker exec -it mysql mysql -u root -proot -h localhost -e "USE tutorial_db; INSERT INTO example_table (id, code, name, datetime) VALUES (100, 100, 'example name 100', CURRENT_TIMESTAMP);"
```

```shell
docker cp example_data.sql mysql:/example_data.sql
docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"
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

### E2eTest

```shell
POD_NAME=mysql
POD_FULL_NAME=$(kubectl get pods -n dev | grep $POD_NAME | awk '{print $1}')
kubectl exec -it $POD_FULL_NAME -n dev -c mysql -- mysql -u user -ppassword -h localhost -e "USE tutorial_db; INSERT INTO example_table (id, code, name, datetime) VALUES (100, 100, 'example name 100', CURRENT_TIMESTAMP);"
```

### Port-Forwarding

```shell
kubectl port-forward service/adminer 8084:8084 -n dev
```

```shell
kubectl port-forward service/kafdrop-service 9000:9000 -n dev
```

```shell
kubectl port-forward service/debeziumui 8082:8082 -n dev
```

```shell
kubectl port-forward service/debezium 8083:8083 -n dev
```

```shell
kubectl port-forward service/application 8080:8080 -n dev
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

* Cockroach DB: [http://localhost:8081](http://localhost:8081)

## CockroachDB

<p style="text-align: justify;">

For more information about CockroachDB see the [www.cockroachlabs.com/docs](https://www.cockroachlabs.com/docs).



```shell
docker compose --file docker-compose.yml --project-name test up --build -d
```

```shell
netstat -ano | findstr :26257
taskkill /PID 12345 /F
```


```shell
docker exec -it cockroachdb cockroach sql --host=localhost:26257 --insecure
```

```postgresql
create database tutorial_db;
show databases;
CREATE USER tutorial_user WITH PASSWORD 'tutorial_password';
```


##

**<p align="center"> [Top](#integration-of-spring-boot-and-cockroachdb) </p>**