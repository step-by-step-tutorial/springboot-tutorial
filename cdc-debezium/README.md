# <p style="text-align: center;">CDC</p>
# <p style="text-align: center;"> Integration of Spring Boot and Debezium</p>

<p style="text-align: justify;">

This tutorial is about the integration of Spring Boot and Debezium to capture changed data.
In this tutorial I use Spring Boot, MySQL, Kafka and Debezium to CDC.

</p>

## <p style="text-align: center;"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [Debezium](#debezium)

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

* Application: [http://localhost:8080](http://localhost:8080)
* MySQL (Adminer): [http://localhost:8084](http://localhost:8084)
* Kafka (kafkadrop): [http://localhost:9000](http://localhost:9000)
* Debezium: [http://localhost:8082](http://localhost:8082)

## Debezium

<p style="text-align: justify;">

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

Connectors are used for establishing a connection between Debezium, Kafka and a database.

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

##

**<p style="text-align: center;"> [Top](#integration-of-spring-boot-and-debezium) </p>**