# <p align="center">Integration of Spring Boot and Embedded Debezium</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Debezium to capture changed data.
In this tutorial I use Spring Boot, MySQL, Kafka and Debezium to CDC.

</p>

## <p align="center"> Table of Content </p>

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
docker build -t samanalishiri/application:latest .
kubectl apply -f ./kube-dev.yml
```

### Check Status

```shell
kubectl get all -n dev
```

### E2eTest

```shell
POD_NAME=mysql
POD_FULL_NAME=$(kubectl get pods -n dev | grep $POD_NAME | awk '{print $1}')
kubectl exec -it $POD_FULL_NAME  -n dev -c mysql -- mysql -u user -ppassword -h localhost -e "USE tutorial_db; INSERT INTO example_table (id, code, name, datetime) VALUES (100, 100, 'example name 100', CURRENT_TIMESTAMP);"
```

### Port-Forwarding

```shell
kubectl port-forward service/adminer 8084:8084 -n dev
```

```shell
kubectl port-forward service/kafdrop-service 9000:9000 -n dev
```

```shell
kubectl port-forward service/application 8080:8080 -n dev
```

### Down

```shell
kubectl delete all --all -n dev
kubectl delete secrets dev-credentials -n dev
kubectl delete configMap dev-config -n dev
kubectl delete persistentvolumeclaim mysql-pvc -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* Application: [http://localhost:8080](http://localhost:8080)
* MySQL (Adminer): [http://localhost:8084](http://localhost:8084)
* Kafka (kafkadrop): [http://localhost:9000](http://localhost:9000)

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

Connectors are used for establishing a connection between Debezium, Kafka and a database.

**General Format**

```java
File dbhistoryFile = new File(System.getProperty("java.io.tmpdir"), "dbhistory.dat");
File offsetsFile = new File(System.getProperty("java.io.tmpdir"), "offsets.dat");
Configuration config = Configuration.create()
        .with("name", "embedded-engine")
        .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
        .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
        .with("offset.storage.file.filename", offsetsFile.getAbsolutePath())
        .with("offset.flush.interval.ms", "60000")
        .with("tasks.max", "1")
        .with("database.server.id", "1")
        .with("database.hostname", host)
        .with("database.port", port)
        .with("database.user", dbUser)
        .with("database.password", dbPassword)
        .with("database.whitelist", dbName)
        .with("table.include.list", tables)
        .with("topic.prefix", topicPrefix)
        .with("schema.history.internal.kafka.bootstrap.servers", kafkaUrl)
        .with("schema.history.internal.kafka.topic", "schema-changes.db")
        .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
        .with("database.history.file.filename", dbhistoryFile.getAbsolutePath())
        .build();
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-embedded-debezium) </p>**