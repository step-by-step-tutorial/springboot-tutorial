# <p align="center">NoSQL Reactive Redis</p>

<p align="justify">

This tutorial is included [Redis](https://redis.io/) configuration for test and none test environment. This tutorial
uses [Lettuce](https://lettuce.io/) to create connection factories for making connection to the Redis.

</p>

## Install Redis on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

```yaml
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
    volumes:
      - "./docker/redislabs:/db"
    ports:
      - "8001:8001"

```

Execute the `docker compose  up -d` command to install Redis and Redisinsight.

```shell
# full command
docker compose --file ./docker-compose.yml --project-name redis up --build -d

```

### Redisinsight

In order to connect to redis by Redisinsight through the web browser open [http://host-IP:8001/](http://host-IP:8001/)
in the web browser then select the `I already have a database` and continue with the following properties.

  ```yaml
  host: IP of the host machine (only IP and not localhost)
  port: 6379
  database: just a name
  ```

### Commander

Also, there is another alternative for the Redisinsight to access redis named Commander.

```yaml
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

In order to connect to redis through web browser by commander open [http://localhost:8081/](http://localhost:8081/) in
the web browser.

## Install Redis on Kubernetes

### Redis

Create the following files for installing Redis.

**redis-deployment.yml**

```yaml
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

**redis-service.yml**

```yaml
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

### Redisinsight

**redisinsight-deployment.yml**

```yaml
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
            - containerPort: 8001
```

**redisinsight-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: redisinsight
spec:
  selector:
    app: redisinsight
  ports:
    - protocol: TCP
      port: 8001
      targetPort: 8001
```

### Apply Configuration Files

Execute the following commands to install the tools on Kubernetes.

```shell
# ======================================================================================================================
# Redis
# ======================================================================================================================
kubectl apply -f ./kube/redis-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment redis -n default

kubectl apply -f ./kube/redis-service.yml
# kubectl get service -n default
# kubectl describe service redis -n default

# ======================================================================================================================
# Redisinsight
# ======================================================================================================================
kubectl apply -f ./kube/redisinsight-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment redisinsight -n default

kubectl apply -f ./kube/redisinsight-service.yml
# kubectl get services -n default
# kubectl describe service redisinsight -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all
```

For connecting to Redis through application on localhost it should be executed the following command.

```shell
# redis
kubectl port-forward service/redis 6379:6379
```

<p align="justify">

In order to connect to Redisinsight from localhost through the web browser use the following command and dashboard of
Redisinsight is available on [http://localhost:8001](http://localhost:8001) URL.

</p>

```shell
# redisinsight
# http://localhost:8001
kubectl port-forward service/redisinsight 8001:8001
```

Then select the `I already have a database` and continue with the following properties.

```yaml
  host: IP of the host machine (only IP and not localhost)
  port: 6379
  database: just a name
```

### Commander

Also, there is another alternative for Redisinsight named Commander. Therefore, use the following instruction to install
that on Kubernetes.

**commander-deployment.yml**

```yaml
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

**commander-service.yml**

```yaml
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

Execute the following commands to install the tools on Kubernetes.

```shell
# ======================================================================================================================
# Commander
# ======================================================================================================================
kubectl apply -f ./kube/commander-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment commander -n default

kubectl apply -f ./kube/commander-service.yml
# kubectl get services -n default
# kubectl describe service commander -n default

kubectl get all
```

<p align="justify">

In order to connect to Commander through the web browser on localhost use the following command and dashboard of
Commander is available on [http://localhost:8081](http://localhost:8081) URL.

</p>

```shell
# commander
# http://localhost:8081
kubectl port-forward service/commander 8081:8081
```

## How To Config Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>
    <dependency>
        <groupId>io.lettuce</groupId>
        <artifactId>lettuce-core</artifactId>
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

### Test Dependency

```xml

<dependency>
    <groupId>com.github.kstyrc</groupId>
    <artifactId>embedded-redis</artifactId>
    <version>0.6</version>
    <scope>test</scope>
</dependency>
```

### Spring Boot Properties

```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      repositories:
        enabled: false
```

## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)

## Build

```bash
mvn clean package -DskipTests=true
```

## Test

```bash
mvn  test
```

## Run

```bash
mvn  spring-boot:run
```

##

**<p align="center"> [Top](#nosql-reactive-redis) </p>**