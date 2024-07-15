# <p align="center">Integration of Spring Boot And Redis</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Redis.

</p>
## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Redis](#redis)
* [Redis Use Cases](#redis-use-cases)
* [Install Redis on Docker](#install-redis-on-docker)
* [Install Redis on Kubernetes](#install-redis-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [License](#license)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Redis](https://redis.io)
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

## Redis

<p align="justify">

This tutorial is included [Redis](https://redis.io) configuration for test and none test environment. There are two
libraries to create connection factories for making connection to the Redis.

* [Jedis](https://redis.io/docs/clients/java/)
* [Lettuce](https://lettuce.io/)

For more information about Redis see the [https://redis.io](https://redis.io).

</p>

## Redis Use Cases

List of use cases for Redis
[https://redis.io/docs/latest/develop/interact/search-and-query/query-use-cases/](https://redis.io/docs/latest/develop/interact/search-and-query/query-use-cases/).

* Application search and external secondary index
* Secondary index for Redis data
* Geo-distributed search
* Unified search
* Analytics
* Ephemeral search (retail)
* Real-time inventory (retail)
* Real-time conversation analysis (telecom)
* Research portal (academia)

## Install Redis on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose File

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: "3.8"

services:
  redis:
    image: redis:latest
    container_name: redis
    hostname: redis
    restart: always
    ports:
      - "6379:6379"
  redisinsight:
    image: redislabs/redisinsight:latest
    container_name: redisinsight
    hostname: redisinsight
    restart: always
    ports:
      - "5540:5540"
  commander:
    image: rediscommander/redis-commander:latest
    container_name: commander
    hostname: commander
    restart: always
    environment:
      - REDIS_HOSTS=local:redis:6379
    ports:
      - "8081:8081"
```

### Apply Docker Compose File

Execute the following command to install Redis.

```shell
docker compose --file ./docker-compose.yml --project-name redis up --build -d

```

### Redisinsight

In order to connect to redis by Redisinsight through the web browser
open [http://localhost:5540](http://localhost:5540).

<p align="center">

<img src="redisinsight-login.png" height="30%" width="30%">

</p>

Then select "Add connection details manually" and enter redis as host and 6379 for port. Also, you can enter an alias
for database.

### Commander

In order to connect to redis by Commander through the web browser open [http://localhost:8081](http://localhost:8081).

## Install Redis on Kubernetes

Create the following files for installing Redis.

### Kube Files

[redis-deployment.yml](./kube/redis-deployment.yml)

```yaml
# redis-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
        - name: redis
          image: redis:latest
          ports:
            - containerPort: 6379
```

[redis-service.yml](./kube/redis-service.yml)

```yaml
# redis-service.yml
apiVersion: v1
kind: Service
metadata:
  name: redis
spec:
  selector:
    app: redis
  ports:
    - protocol: TCP
      port: 6379
      targetPort: 6379
```

[redisinsight-deployment.yml](./kube/redisinsight-deployment.yml)

```yaml
# redisinsight-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redisinsight
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redisinsight
  template:
    metadata:
      labels:
        app: redisinsight
    spec:
      containers:
        - name: redisinsight
          image: redislabs/redisinsight:latest
          ports:
            - containerPort: 5540
```

[redisinsight-service.yml](./kube/redisinsight-service.yml)

```yaml
# redisinsight-service.yml
apiVersion: v1
kind: Service
metadata:
  name: redisinsight
spec:
  selector:
    app: redisinsight
  ports:
    - protocol: TCP
      port: 5540
      targetPort: 5540
```

[commander-deployment.yml](/kube/commander-deployment.yml)

```yaml
#commander-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: commander-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: commander
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: commander
    spec:
      containers:
        - name: commander
          image: rediscommander/redis-commander:latest
          ports:
            - containerPort: 8081
          env:
            - name: REDIS_HOST
              value: redis
            - name: REDIS_PORT
              value: "6379"
```

[commander-service.yml](/kube/commander-service.yml)

```yaml
#commander-service.yml
apiVersion: v1
kind: Service
metadata:
  name: commander
spec:
  selector:
    app: commander
  ports:
    - port: 8081
      targetPort: 8081
      name: commander
      protocol: TCP
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/redis-deployment.yml
kubectl apply -f ./kube/redis-service.yml
kubectl apply -f ./kube/redisinsight-deployment.yml
kubectl apply -f ./kube/redisinsight-service.yml
kubectl apply -f ./kube/commander-deployment.yml
kubectl apply -f ./kube/commander-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Redis from localhost through the application use the following command and Redis is available
on `localhost:6379`.

</p>

```shell
kubectl port-forward service/redis 6379:6379
```

<p align="justify">

In order to connect to Redisinsight from localhost through the web browser use the following command and dashboard of
Redisinsight is available on [http://localhost:5540](http://localhost:5540) URL.

</p>

```shell
kubectl port-forward service/redisinsight 5540:5540
```

<p align="justify">

In order to connect to Commander from localhost through the web browser use the following command and dashboard of
Commander is available on [http://localhost:8081](http://localhost:8081) URL.

</p>

```shell
kubectl port-forward service/commander 8081:8081
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>io.lettuce</groupId>
        <artifactId>lettuce-core</artifactId>
    </dependency>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifac>jedis</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```

### Application Properties

```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      repositories:
        enabled: false
```

## How To Set up Spring Boot Test

### Dependency

```xml

<dependency>
    <groupId>com.github.kstyrc</groupId>
    <artifactId>embedded-redis</artifactId>
    <version>0.6</version>
    <scope>test</scope>
</dependency>
```

## Appendix

### Makefile

```shell
docker-deploy:
	docker compose --file docker-compose.yml --project-name redis up -d

docker-rebuild-deploy:
	docker compose --file docker-compose.yml --project-name redis up --build -d

docker-remove-container:
	docker rm redis --force
	docker rm redisinsight --force
	docker rm commander --force

docker-remove-image:
	docker image rm redis:latest
	docker image rm redislabs/redisinsight:latest
	docker image rm rediscommander/redis-commander:latest

kube-deploy:
	kubectl apply -f ./kube/redis-deployment.yml
	kubectl apply -f ./kube/redis-service.yml
	kubectl apply -f ./kube/redisinsight-deployment.yml
	kubectl apply -f ./kube/redisinsight-service.yml
	kubectl apply -f ./kube/commander-deployment.yml
	kubectl apply -f ./kube/commander-service.yml

kube-remove:
	kubectl delete all --all

kube-port-forward-redis:
	kubectl port-forward service/redis 6379:6379

kube-port-forward-redisinsight:
	kubectl port-forward service/redisinsight 5540:5540

kube-port-forward-commander:
	kubectl port-forward service/commander 8081:8081
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-redis) </p>**