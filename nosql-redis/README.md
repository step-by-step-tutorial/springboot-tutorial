# <p align="center">Integration of Spring Boot And Redis</p>

<p align="justify">

This tutorial is about the integration of Spring Boot and Redis.

</p>

<p align="justify">

This tutorial is included [Redis](https://redis.io) configuration for test and none test environment. There are two
libraries to create connection factories for making connection to the Redis.

* [Jedis](https://redis.io/docs/clients/java/)
* [Lettuce](https://lettuce.io/)

For more information about Redis see the [https://redis.io](https://redis.io).

</p>

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
kubectl port-forward service/commander 8081:8081 -n dev
```

```shell
kubectl port-forward service/redisinsight 5540:5540 -n dev
```

```shell
kubectl port-forward service/redis 6379:6379 -n dev
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

### Redisinsight

In order to connect to redis by Redisinsight through the web browser
open [http://localhost:5540](http://localhost:5540).

<p align="center">

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/nosql-redis/doc/redisinsight-login.png" height="30%" width="30%">

</p>

Then select "Add connection details manually" and enter redis as host and 6379 for port. Also, you can enter an alias
for database.

### Commander

In order to connect to redis by Commander through the web browser, open [http://localhost:8081](http://localhost:8081).

##

**<p align="center"> [Top](#integration-of-spring-boot-and-redis) </p>**