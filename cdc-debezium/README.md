# <p align="center">Integration of Spring Boot and Debezium</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Debezium to capture changed data.
In this tutorial I use Spring Boot, MySQL, Kafka and Debezium to CDC.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Debezium](#debezium)
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
docker exec -it mysql mysql -u root -proot -h localhost -e "USE tutorial_db; INSERT INTO example_table (id, code, name, datetime) VALUES (100, 100, 'example name 100', CURRENT_TIMESTAMP);"
```

```shell
docker cp example_data.sql mysql:/example_data.sql
```

```shell
docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"
```
Check the application console log.

### Stop

```shell
mvn  spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
```

## Debezium

<p align="justify">

Debezium is an open source distributed platform for change data capture (CDC). For more information about Debezium see
the [https://debezium.io](https://debezium.io).

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

#### Add Connectors

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

#### List of Connectors

```shell
curl -i -X GET http://localhost:8083/connectors -H "Accept:application/json"
```

#### Get Connector

```shell
curl -i -X GET http://localhost:8083/connectors/connectorname -H "Accept:application/json"
```

```shell
# example
curl -i -X GET http://localhost:8083/connectors/spring-boot-tutorial -H "Accept:application/json"
```

#### Delete Connector

```shell
curl -i -X DELETE http://localhost:8083/connectors/connectorname
```

```shell
# example
curl -i -X DELETE http://localhost:8083/connectors/spring-boot-tutorial
```

## Dockerized

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
name: dev-env
services:
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
      - "./users.sql:/docker-entrypoint-initdb.d/users.sql"
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
    depends_on:
      - mysql
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
    environment:
      KAFKA_CFG_BROKER_ID: 1
      ALLOW_PLAINTEXT_LISTENER: yes
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_LISTENERS: LOCALHOST://:9092,CONTAINER://:9093
      KAFKA_CFG_ADVERTISED_LISTENERS: LOCALHOST://localhost:9092,CONTAINER://kafka:9093
      KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: LOCALHOST:PLAINTEXT,CONTAINER:PLAINTEXT
      KAFKA_CFG_INTER_BROKER_LISTENER_NAME: LOCALHOST
    depends_on:
      - zookeeper
    healthcheck:
      test: [ "CMD", "kafka-broker-api-versions.sh", "--bootstrap-server", "localhost:9092" ]
      interval: 10s
      timeout: 5s
      retries: 5
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
    depends_on:
      - kafka
  debezium:
    image: debezium/connect:3.0.0.Final
    container_name: debezium
    hostname: debezium
    ports:
      - "8083:8083"
    environment:
      GROUP_ID: 1
      CONFIG_STORAGE_TOPIC: debezium-config
      OFFSET_STORAGE_TOPIC: debezium-offset
      STATUS_STORAGE_TOPIC: debezium-status
      BOOTSTRAP_SERVERS: kafka:9093
    depends_on:
      mysql:
        condition: service_healthy
      kafka:
        condition: service_healthy
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
    depends_on:
      - debezium
```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker compose --file docker-compose.yml --project-name dev-env up --build -d
```

### E2eTest

```shell
docker exec -it mysql mysql -u root -proot -h localhost -e "USE tutorial_db; INSERT INTO example_table (id, code, name, datetime) VALUES (100, 100, 'example name 100', CURRENT_TIMESTAMP);"
```

```shell
docker cp example_data.sql mysql:/example_data.sql
```

```shell
docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev-env down
```

## Kubernetes

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
          imagePullPolicy: "IfNotPresent"
          ports:
            - containerPort: 8083
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
              value: "kafka-service:9093"
---
apiVersion: v1
kind: Service
metadata:
  name: debezium
spec:
  selector:
    app: debezium
  ports:
    - port: 8083
      targetPort: 8083
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
              value: http://debezium:8083
---
apiVersion: v1
kind: Service
metadata:
  name: debeziumui
spec:
  selector:
    app: debeziumui
  ports:
    - port: 8082
      targetPort: 8080
```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker build -t samanalishiri/application:latest .
```

```shell
kubectl apply -f ./kube/mysql.yml
```

```shell
kubectl apply -f ./kube/kafka.yml
```

```shell
kubectl apply -f ./kube/debezium.yml
```

```shell
kubectl apply -f ./kube/application.yml
```

### Check Status

```shell
kubectl get all
```

### E2eTest

Use this command `kubectl get pods` to see the mysql pod-id.

```shell
kubectl get pods
```

```shell
kubectl exec -it mysql-??? -n default -c mysql -- mysql -u user -ppassword -h localhost -e "USE tutorial_db; INSERT INTO example_table (id, code, name, datetime) VALUES (100, 100, 'example name 100', CURRENT_TIMESTAMP);"
```

### Port-Forwarding

```shell
kubectl port-forward service/adminer 8084:8084
```

```shell
kubectl port-forward service/kafdrop-service 9000:9000
```

```shell
kubectl port-forward service/debeziumui 8082:8082
```

```shell
kubectl port-forward service/debezium 8083:8083
```

```shell
kubectl port-forward service/application 8080:8080
```

### Down

```shell
kubectl delete all --all
```

```shell
kubectl delete secrets mysql-credentials
```

```shell
kubectl delete configMap mysql-config
```

```shell
kubectl delete persistentvolumeclaim database-pvc
```

```shell
docker image rm samanalishiri/application:latest
```

## UI

* Application: [http://localhost:8080](http://localhost:8080)
* MySQL (Adminer): [http://localhost:8084](http://localhost:8084)
* Kafka (kafkadrop): [http://localhost:9000](http://localhost:9000)
* Debezium: [http://localhost:8082](http://localhost:8082)

##

**<p align="center"> [Top](#integration-of-spring-boot-and-debezium) </p>**