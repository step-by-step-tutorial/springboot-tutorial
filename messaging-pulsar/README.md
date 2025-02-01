# <p align="center">Integration of Spring Boot And Apache Pulsar</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Pulsar.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Pulsar](#pulsar)
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

## Pulsar

<p align="justify">

For more information about Pulsar see the [https://pulsar.apache.org/docs](https://pulsar.apache.org/docs).

</p>

### Use Cases

* Real-Time Data Streaming and Analytics
* Event-Driven Architectures
* Messaging System (Pub/Sub & Queueing)
* Multi-Tenant Cloud-Native Applications
* Log and Event Aggregation
* IoT Data Processing
* Video and Media Streaming
* Edge Computing & 5G Applications
* Machine Learning Pipelines
* Hybrid Cloud and Multi-Cloud Messaging

## Dockerized

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
name: dev-env
networks:
  pulsar:
    driver: bridge
services:
  zookeeper:
    image: apachepulsar/pulsar:latest
    container_name: zookeeper
    hostname: zookeeper
    restart: on-failure
    ports:
      - "2181:2181"
    networks:
      - pulsar
    volumes:
      - ./data/zookeeper:/pulsar/data/zookeeper
    environment:
      - metadataStoreUrl=zk:zookeeper:2181
      - PULSAR_MEM=-Xms256m -Xmx256m -XX:MaxDirectMemorySize=256m
    command: >
      bash -c "bin/apply-config-from-env.py conf/zookeeper.conf && \
             bin/generate-zookeeper-config.sh conf/zookeeper.conf && \
             exec bin/pulsar zookeeper"
    healthcheck:
      test: [ "CMD", "bin/pulsar-zookeeper-ruok.sh" ]
      interval: 10s
      timeout: 5s
      retries: 30
  pulsar-init:
    container_name: pulsar-init
    hostname: pulsar-init
    image: apachepulsar/pulsar:latest
    networks:
      - pulsar
    command: >
      bash -c "bin/pulsar initialize-cluster-metadata \
      --cluster cluster-a \
      --zookeeper zookeeper:2181 \
      --configuration-store zookeeper:2181 \
      --web-service-url http://broker:8080 \
      --broker-service-url pulsar://broker:6650"
    depends_on:
      zookeeper:
        condition: service_healthy
  bookie:
    image: apachepulsar/pulsar:latest
    container_name: bookie
    hostname: bookie
    restart: on-failure
    ports:
      - "3181:3181"
    networks:
      - pulsar
    environment:
      - clusterName=cluster-a
      - zkServers=zookeeper:2181
      - metadataServiceUri=metadata-store:zk:zookeeper:2181
      # otherwise every time we run docker compose uo or down we fail to start due to Cookie
      # See: https://github.com/apache/bookkeeper/blob/405e72acf42bb1104296447ea8840d805094c787/bookkeeper-server/src/main/java/org/apache/bookkeeper/bookie/Cookie.java#L57-68
      - advertisedAddress=bookie
      - BOOKIE_MEM=-Xms512m -Xmx512m -XX:MaxDirectMemorySize=256m
    depends_on:
      zookeeper:
        condition: service_healthy
      pulsar-init:
        condition: service_completed_successfully
    # Map the local directory to the container to avoid bookie startup failure due to insufficient container disks.
    volumes:
      - ./data/bookkeeper:/pulsar/data/bookkeeper
    command: bash -c "bin/apply-config-from-env.py conf/bookkeeper.conf && exec bin/pulsar bookie"

  broker:
    image: apachepulsar/pulsar:latest
    container_name: broker
    hostname: broker
    restart: on-failure
    networks:
      - pulsar
    environment:
      - metadataStoreUrl=zk:zookeeper:2181
      - zookeeperServers=zookeeper:2181
      - clusterName=cluster-a
      - managedLedgerDefaultEnsembleSize=1
      - managedLedgerDefaultWriteQuorum=1
      - managedLedgerDefaultAckQuorum=1
      - advertisedAddress=broker
      - advertisedListeners=external:pulsar://127.0.0.1:6650
      - PULSAR_MEM=-Xms512m -Xmx512m -XX:MaxDirectMemorySize=256m
    depends_on:
      zookeeper:
        condition: service_healthy
      bookie:
        condition: service_started
    ports:
      - "6650:6650"
      - "8081:8080"
    command: bash -c "bin/apply-config-from-env.py conf/broker.conf && exec bin/pulsar broker"
  dashboard:
    image: apachepulsar/pulsar-manager:latest
    container_name: dashboard
    hostname: dashboard
    ports:
      - "9527:9527"
      - "7750:7750"
    networks:
      - pulsar
    depends_on:
      - broker
    links:
      - broker
    environment:
      SPRING_CONFIGURATION_FILE: /pulsar-manager/pulsar-manager/application.properties
```

### Deploy

Execute the following command to install Pulsar.

```shell
mvn clean package verify -DskipTests=true
```

#### Cluster Mode

```shell
docker compose --file ./docker-compose.yml --project-name dev-env up --build -d
```

#### Standalone Mode

```shell
docker compose --file ./docker-compose-standalone.yml --project-name dev-env up --build -d
```

### New Environment

```shell
CSRF_TOKEN=$(curl http://localhost:7750/pulsar-manager/csrf-token)
curl -X PUT http://localhost:7750/pulsar-manager/users/superuser \
   -H 'X-XSRF-TOKEN: $CSRF_TOKEN' \
   -H 'Cookie: XSRF-TOKEN=$CSRF_TOKEN;' \
   -H "Content-Type: application/json" \
   -d '{"name": "admin", "password": "password", "description": "administrator", "email": "admin@email.com"}'
```

Pulsar Dashboard: [http://localhost:9527](http://localhost:9527)

```yaml
environment-name: docker.local
broker-url: http://broker:8080
bookie-url: http://bookie:3181
```

### API

```shell
curl -X PUT http://localhost:8081/admin/v2/persistent/public/default/test-topic-2/partitions -H 'Content-Type: application/json' -d "4"
```

```shell
curl -X POST http://localhost:8081/admin/v2/persistent/public/default/test-topic-2/partitions -H 'Content-Type: application/json' -d "5"
```

```shell
curl -X GET http://localhost:8081/admin/v2/persistent/public/default/test-topic-2/partitioned-internalStats | jq
```

```shell
curl -X GET http://localhost:8081/admin/v2/persistent/public/default | jq
```

```shell
curl -X DELETE http://localhost:8081/admin/v2/persistent/public/default/test-topic-2/partitions
```

```shell
CSRF_TOKEN=$(curl http://localhost:7750/pulsar-manager/csrf-token)
echo $CSRF_TOKEN
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

Create the following files for installing Pulsar.

### Kube Files

[tools_name-deployment.yml](/kube/tools_name-deployment.yml)

```yaml
#deployment.yml
```

[tools_name-service.yml](/kube/tools_name-service.yml)

```yaml
#service.yml
```

### Deploy

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

In order to connect to Pulsar from localhost through the web browser use the following command and dashboard of
Pulsar is available on [http://localhost:port](http://localhost:port) URL.

</p>

```shell
kubectl port-forward service/tools_name port:port
```

### E2eTest

```shell
kubectl port-forward service/application 8080:8080
```

```shell
```

### Down

```shell
kubectl delete all --all
```

```shell
kubectl delete secrets ???
```

```shell
kubectl delete configMap ???
```

```shell
docker image rm samanalishiri/application:latest
```

## UI

* Application: [http://localhost:8080](http://localhost:8080)
* Pulsar Admin Dashboard: [http://localhost:8081](http://localhost:8081)
* Pulsar Dashboard: [http://localhost:9527](http://localhost:9527)

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-pulsar) </p>**
