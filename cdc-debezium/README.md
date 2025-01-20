# <p align="center">Integration of Spring Boot and Debezium</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Debezium to capture changed data.
In this tutorial I use Spring Boot, MySQL, Kafka and Debezium to CDC.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Debezium](#debezium)
* [Dockerized](#dockerized)
* [Cloud-Native](#cloud-native)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

### Build

```shell
mvn clean package -DskipTests=true 
```

### Test

```shell
mvn test
```

### Run

```shell
mvn  spring-boot:run
```

### Pipeline

```shell
make LocalPipeline
```

```shell
make DockerizedPipeline
```

```shell
make CloudNativePipeline
```
```shell
kubectl exec -it mysql-6dd5c5fdbb-flbsb -n default -c mysql -- mysql -u user -ppassword -h localhost
```

```shell
make E2eTest
```

```shell
docker compose --file ./docker-compose.yml --project-name dev-env down
```

```shell
make CloudNativeDown
```

```shell
kubectl port-forward service/debeziumui 8082:8082
```

## Debezium

<p align="justify">

Debezium is an open source distributed platform for change data capture (CDC). For more information about Debezium see
the [https://debezium.io](https://debezium.io).

### Connector

Connectors use for establish a connection between Debezium, Kafka and a database.

**General Format**

```json
{
  "name": "connectorname",
  "config": {
    "connector.class": "io.debezium.connector....",
    "tasks.max": "1",
    "database.hostname": "hostname",
    "database.port": "port",
    "database.user": "username",
    "database.password": "password",
    "database.server.id": "a number",
    "database.server.name": "servername",
    "database.whitelist": "databases name",
    "table.include.list": "a comma separate list of tables name include schema name like schemaname.tablename, ...",
    "schema.history.internal.kafka.bootstrap.servers": "kafkaurl",
    "schema.history.internal.kafka.topic": "a name like schema-changes.db",
    "topic.prefix": "a word use as prefix",
    "include.schema.changes": "true",
    "transforms": "unwrap",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState"
  }
}
```

**MySQL Connector**

```json
 {
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
    "database.whitelist": "tutorial_db",
    "table.include.list": "tutorial_db.example_table",
    "schema.history.internal.kafka.bootstrap.servers": "kafka:9093",
    "schema.history.internal.kafka.topic": "schema-changes.db",
    "topic.prefix": "cdc",
    "include.schema.changes": "true",
    "transforms": "unwrap",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState"
  }
}
```

</p>

### Use Cases

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

## Dockerized

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: '3.9'
name: dev-env
services:
  redis:
    image: redis:latest
    container_name: redis
    hostname: redis
    restart: always
    ports:
      - "6379:6379"
    healthcheck:
      test: [ "CMD", "redis-cli", "ping" ]
      interval: 10s
      timeout: 5s
      retries: 5
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
      - MYSQL_DATABASE=tutorial_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./src/main/resources/users.sql:/docker-entrypoint-initdb.d/users.sql"
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "--password=root" ]
      interval: 10s
      timeout: 5s
      retries: 5
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
  debezium:
    image: debezium/connect:3.0.0.Final
    container_name: debezium
    hostname: debezium
    ports:
      - "8083:8083"
    depends_on:
      - kafka
      - mysql
    environment:
      GROUP_ID: 1
      CONFIG_STORAGE_TOPIC: debezium-config
      OFFSET_STORAGE_TOPIC: debezium-offset
      STATUS_STORAGE_TOPIC: debezium-status
      BOOTSTRAP_SERVERS: kafka:9093
    healthcheck:
      test: [ "CMD", "curl", "-f", "http://localhost:8083" ]
      interval: 10s
      timeout: 5s
      retries: 5
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

### Deploy

Execute the following command to install Debezium stack.

```shell
docker compose --file ./docker-compose.yml --project-name dev-env up --build -d
```

### DOWN

Execute the following command to stop and remove Debezium stack.

```shell
docker compose --file ./docker-compose.yml --project-name dev-env down
```

### Set up Connectors

#### Add Debezium Connectors Via Restful Web service

```shell
curl -i -X POST http://localhost:8083/connectors \
-H "Accept:application/json" \
-H 'Content-Type: application/json' \
-d '{
  "name": "connectorname",
  "config": {
    "connector.class": "io.debezium.connector....",
    "tasks.max": "1",
    "database.hostname": "hostname",
    "database.port": "port",
    "database.user": "username",
    "database.password": "password",
    "database.server.id": "a number",
    "database.server.name": "servername",
    "database.whitelist": "databases name",
    "table.include.list": "a comma separate list of tables name include schema name like schemaname.tablename, ...",
    "schema.history.internal.kafka.bootstrap.servers": "kafkaurl",
    "schema.history.internal.kafka.topic": "a name like schema-changes.db",
    "topic.prefix": "a word use as prefix",
    "include.schema.changes": "true",
    "transforms": "unwrap",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState"
  }
}'
```

Example of the request for MySQL and Kafka.

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
    "database.user": "username",
    "database.password": "password",
    "database.server.id": "1",
    "database.server.name": "mysql",
    "database.whitelist": "tutorial_db",
    "table.include.list": "tutorial_db.example_table",
    "schema.history.internal.kafka.bootstrap.servers": "kafka:9093",
    "schema.history.internal.kafka.topic": "schema-changes.db",
    "topic.prefix": "cdc",
    "include.schema.changes": "true",
    "transforms": "unwrap",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState"
  }
}'
```

#### List of Debezium Connectors

```shell
curl -i -X GET http://localhost:8083/connectors -H "Accept:application/json"
```

#### Get Debezium Connector

```shell
curl -i -X GET http://localhost:8083/connectors/connectorname -H "Accept:application/json"
```

```shell
# example
curl -i -X GET http://localhost:8083/connectors/spring-boot-tutorial -H "Accept:application/json"
```

#### Delete Debezium Connector

```shell
curl -i -X DELETE http://localhost:8083/connectors/connectorname
```

```shell
# example
curl -i -X DELETE http://localhost:8083/connectors/spring-boot-tutorial
```

### Test

#### Solution 1
Connect to containerized MySQL.
```shell
docker exec -it mysql mysql -u root -proot -h localhost
```
Execute the following query. Then see the logs.
```mysql
USE tutorial_db;
INSERT INTO example_table (id, code, name, datetime) VALUES (100, 100, 'example name 100', CURRENT_TIMESTAMP);
```

#### Solution 2
Copy SQL file to MySQL container.
```shell
docker cp example_data.sql mysql:/example_data.sql
```
Execute the SQL file. Then see the logs.
```shell
docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"
```

## Cloud-Native

Create the following files for installing Debezium.

### Kube Files

[debezium.yml](/kube/debezium.yml)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: debezium
spec:
  replicas: 1
  selector:
    matchLabels:
      app: debezium
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: debezium
    spec:
      containers:
        - name: debezium
          image: debezium/connect:3.0.0.Final
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
          env:
            - name: GROUP_ID
              value: "1"
            - name: CONFIG_STORAGE_TOPIC
              value: debezium-config
            - name: OFFSET_STORAGE_TOPIC
              value: debezium-offset
            - name: STATUS_STORAGE_TOPIC
              value: debezium-status
            - name: BOOTSTRAP_SERVERS
              value: kafka:9093
---
apiVersion: v1
kind: Service
metadata:
  name: debezium
spec:
  selector:
    app: debezium
  ports:
    - port: 8080
      targetPort: 8080
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: debeziumui
spec:
  replicas: 1
  selector:
    matchLabels:
      app: debeziumui
  template:
    metadata:
      labels:
        app: debeziumui
    spec:
      containers:
        - name: debeziumui
          image: debezium/debezium-ui:latest
          ports:
            - containerPort: 8080
          env:
            - name: KAFKA_CONNECT_URIS
              value: http://debezium:8080
---
apiVersion: v1
kind: Service
metadata:
  name: debeziumui
spec:
  selector:
    app: debeziumui
  ports:
    - port: 8080
      targetPort: 8080
```

### Deploy

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/mysql.yml
kubectl apply -f ./kube/kafka.yml
kubectl apply -f ./kube/debezium.yml
kubectl apply -f ./kube/application.yml
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

You need to set up a database like MySQL, Kafka and Debzium.

### Dependencies

```xml

<dependencies>
    <!--spring-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-docker-compose</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!--spring-->
    <!--drivers-->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
    </dependency>
    <!--drivers-->
    <!--embedded debezium-->
    <dependency>
        <groupId>io.debezium</groupId>
        <artifactId>debezium-api</artifactId>
        <version>2.6.0.Final</version>
    </dependency>
    <dependency>
        <groupId>io.debezium</groupId>
        <artifactId>debezium-embedded</artifactId>
        <version>2.6.0.Final</version>
    </dependency>
    <dependency>
        <groupId>io.debezium</groupId>
        <artifactId>debezium-connector-mysql</artifactId>
        <version>2.6.0.Final</version>
    </dependency>
    <!--embedded debezium-->
</dependencies>
```

### Application Properties

```yaml
# mysql
spring:
  datasource:
    username: ${DATABASE_USERNAME:user}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:mysql://${DATABASE_HOST:localhost}:${DATABASE_PORT:3306}/${DATABASE_NAME:tutorial_db}
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: MYSQL
    database-platform: org.hibernate.dialect.MySQL8Dialect
    defer-datasource-initialization: true
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      javax:
        persistence:
          create-database-schemas: true
      hibernate:
        generate_statistics: true
        format_sql: true
        naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
```

```yaml
# kafka
spring:
  kafka:
    topic:
      name: ${KAFKA_TOPIC_NAME:cdc.tutorial_db.example_table}
    consumer:
      group-id: ${KAFKA_GROUP_ID:cdc.tutorial_db.main-group}
    bootstrap-servers: ${KAFKA_URL:localhost:9092}
```

## How To Set up Spring Boot Test

### Dependencies

```xml

<dependencies>
    <!--test-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mysql</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.redis</groupId>
        <artifactId>testcontainers-redis</artifactId>
        <scope>test</scope>
    </dependency>
    <!--test-->
</dependencies>
```

### Application Properties

```yaml
spring:
  kafka:
    topic:
      name: cdc.tutorial_db.example_table
    consumer:
      group-id: cdc.tutorial_db.main-group
    bootstrap-servers: ${KAFKA_URL:localhost:9092}
```

## Appendix

### Makefile

```makefile
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file docker-compose.yml --project-name dev-env up --build -d

DockerComposeDown:
	docker compose --file docker-compose.yml --project-name dev-env down

DockerRemoveImage:
	docker image rm samanalishiri/application:last

LocalPipeline:
	mvn clean package -DskipTests=true
	mvn test
	mvn spring-boot:run

DockerizedPipeline:
	mvn clean package -DskipTests=true
	mvn test
	docker compose --file docker-compose.yml --project-name dev-env up --build -d

e2e-test:
	docker cp example_data.sql mysql:/example_data.sql
	docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-debezium) </p>**