# <p align="center">NewSQL VoltDB</p>

# <p align="center">Integration of Spring Boot and VoltDB</p>

<p align="justify">

This tutorial is about the integration of Spring Boot and VoltDB.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [VoltDB](#voltdb)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

### Build

```shell
LICENSE_FILE_PATH=/Users/saman/voltdblicense.xml
LICENSE_FILE_PATH=C:\Users\saman\voltdblicense.xml
export LICENSE_FILE_PATH


git clone https://github.com/VoltDB/TollCollectDemo

cd TollCollectDemo
mvn clean package

cd dev-edition-app/target/dev-edition-app-1.0-SNAPSHOT/dev-edition-app/

docker compose up -d
```


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

## VoltDB

<p align="justify">

For more information about Debezium see
the [https://github.com/VoltDB/TollCollectDemo](https://github.com/VoltDB/TollCollectDemo/tree/master)
or [https://www.voltactivedata.com/resources/developer-edition-quick-start-guide](https://www.voltactivedata.com/resources/developer-edition-quick-start-guide).

##

**<p align="center"> [Top](#integration-of-spring-boot-and-voltdb) </p>**