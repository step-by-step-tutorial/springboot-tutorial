# <p align="center">Integration of Spring Boot and Debezium</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Debezium.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Debezium](#debezium)
* [Debezium Use Cases](#debezium-use-cases)
* [Install Debezium on Docker](#install-debezium-on-docker)
* [Install Debezium on Kubernetes](#install-debezium-on-kubernetes)
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

```shell
mvn clean package -DskipTests=true 
```

#### Test

```shell
mvn test
```

#### Run

```shell
mvn  spring-boot:run
```

## Debezium

<p align="justify">

Debezium is an open source distributed platform for change data capture (CDC). For more information about Debezium see
the [https://debezium.io](https://debezium.io).

</p>

## Debezium Use Cases

* Real-Time Data Integration
* Event-Driven Architectures
* Microservices Synchronization
* Data Replication
* Data Migration
* Audit Logging and Compliance
* Real-Time Analytics and Monitoring
* Data Synchronization for Caching
* CDC for Legacy Systems
* Data Recovery and Backup

## Install Debezium on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: '3.9'
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
  mysql:
    image: mysql:8.0
    container_name: mysql
    hostname: mysql
    restart: always
    ports:
      - "3306:3306"
    environment:
      - MYSQL_USER=user
      - MYSQL_PASSWORD=password
      - MYSQL_DATABASE=test_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql"
  adminer:
    image: adminer
    container_name: adminer
    hostname: adminer
    restart: always
    ports:
      - "8084:8080"
  zookeeper:
    image: docker.io/bitnami/zookeeper
    container_name: zookeeper
    hostname: zookeeper
    restart: always
    ports:
      - "2181:2181"
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    image: docker.io/bitnami/kafka
    container_name: kafka
    hostname: kafka
    restart: always
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper
    environment:
      KAFKA_CFG_BROKER_ID: 1
      ALLOW_PLAINTEXT_LISTENER: yes
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_LISTENERS: LOCALHOST://:9092,CONTAINER://:9093
      KAFKA_CFG_ADVERTISED_LISTENERS: LOCALHOST://localhost:9092,CONTAINER://kafka:9093
      KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: LOCALHOST:PLAINTEXT,CONTAINER:PLAINTEXT
      KAFKA_CFG_INTER_BROKER_LISTENER_NAME: LOCALHOST
  debezium:
    image: debezium/connect:3.0.0.Final
    container_name: debezium
    hostname: debezium
    ports:
      - "8083:8083"
    depends_on:
      - kafka
    environment:
      GROUP_ID: 1
      CONFIG_STORAGE_TOPIC: debezium-config
      OFFSET_STORAGE_TOPIC: debezium-offset
      STATUS_STORAGE_TOPIC: debezium-status
      BOOTSTRAP_SERVERS: kafka:9093
  kafdrop:
    image: obsidiandynamics/kafdrop:latest
    container_name: kafdrop
    hostname: kafdrop
    restart: always
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKERCONNECT: kafka:9093
      JVM_OPTS: "-Xms32M -Xmx64M"
  debeziumui:
    image: debezium/debezium-ui:latest
    container_name: debeziumui
    hostname: debeziumui
    ports:
      - "8082:8080"
    environment:
      - KAFKA_CONNECT_URIS=http://debezium:8083
    restart: always

```

### Apply Docker Compose

Execute the following command to install Debezium.

```shell
docker compose --file ./docker-compose.yml --project-name cdc-stack up --build -d
```

#### Add Debezium Config
```shell
curl -i -X POST http://localhost:8083/connectors \
-H "Accept:application/json" \
-H 'Content-Type: application/json' \
-d '{
  "name": "spring-boot-tutorial",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "database.hostname": "mysql",
    "database.port": "3306",
    "database.user": "user",
    "database.password": "password",
    "database.server.id": "1",
    "database.server.name": "mysql",
    "database.whitelist": "test_db",
    "table.include.list": "test_db.sample_table",
    "schema.history.internal.kafka.bootstrap.servers": "kafka:9093",
    "schema.history.internal.kafka.topic": "schema-changes.db",
    "topic.prefix": "cdc",
    "include.schema.changes": "true",
    "transforms": "unwrap",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState"
  }
}'
```
#### List of Debezium Configs
```shell
curl -i -X GET http://localhost:8083/connectors -H "Accept:application/json"
```
#### Get Debezium Config
```shell
curl -i -X GET http://localhost:8083/connectors/spring-boot-tutorial -H "Accept:application/json"
```
#### Delete Debezium Config
```shell
curl -i -X DELETE http://localhost:8083/connectors/spring-boot-tutorial
```

#### Test Config
```shell
docker exec -it mysql mysql -u root -p -h localhost
# password: root
```

```mysql
USE test_db;
INSERT INTO sample_table (id, code, name, datetime) VALUES(1, 1, 'test name 1', CURRENT_TIMESTAMP);
INSERT INTO sample_table (id, code, name, datetime) VALUES (2, 2, 'test name 2', CURRENT_TIMESTAMP);
```

## Install Debezium on Kubernetes

Create the following files for installing Debezium.

### Kube Files

[debezium-deployment.yml](/kube/debezium-deployment.yml)

```yaml
#deployment.yml
```

[debezium-service.yml](/kube/debezium-service.yml)

```yaml
#service.yml
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/debezium-deployment.yml
kubectl apply -f ./kube/debezium-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Debezium from localhost through the web browser use the following command and dashboard of
Debezium is available on [http://localhost:port](http://localhost:port) URL.

</p>

```shell
kubectl port-forward service/debezium port:port
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

```makefile
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