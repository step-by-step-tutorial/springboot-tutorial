# <p align="center">Integration of Spring Boot And PostgreSQL</p>

<p align="justify">
This tutorial demonstrates the integration of Spring Boot and [PostgreSQL](https://www.postgresql.org/), including configuration for test and non-test environments.
</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [PostgreSQL](#postgresql)

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
mvn spring-boot:start
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Stop

```shell
mvn spring-boot:stop
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
# PostgreSQL
kubectl port-forward service/postgresql 5432:5432 -n dev

# PgAdmin
kubectl port-forward service/pgadmin 8081:80 -n dev

# Adminer
kubectl port-forward service/adminer 8080:8080 -n dev

# Application
kubectl port-forward service/application 8080:8080 -n dev
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
kubectl delete persistentvolumeclaim postgres-pvc -n dev
kubectl delete persistentvolumeclaim pgadmin-pvc -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* PgAdmin: [http://localhost:8081](http://localhost:8081)
* Adminer: [http://localhost:8080](http://localhost:8080)

---

# PostgreSQL

<details>
<summary>Show PostgreSQL Training</summary>

## PostgreSQL

<p align="justify">
For more information about PostgreSQL see the [PostgreSQL database](https://www.postgresql.org/).
</p>

### URL

The URL follows the syntax that mentioned below.

```yaml
url: jdbc:postgresql://host:port/database-name
```

### Service Commands

```shell
# try to connect
sudo -u postgres psql postgres

# change password
alter user postgres with password 'password';

# exit from postgres database
type: \q

# how to create user?
sudo -u postgres createuser -D -A -P user-name
# example
sudo -u postgres createuser -D -A -P test_user

# how to create database?
sudo -u postgres createdb -O user-name db-name
# example
sudo -u postgres createdb -O test_user test_db

# how to run sql file?
sudo -u user-name psql -d db-name -f file-name.sql
# example
sudo -u test_user psql -d test_db -f test_db_schema.sql
```

</details>

**<p align="center"> [Top](#integration-of-spring-boot-and-postgresql) </p>**