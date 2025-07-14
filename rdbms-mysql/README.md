# <p align="center">Integration of Spring Boot And MySQL</p>

<p align="justify">
This tutorial demonstrates the integration of Spring Boot and [MySQL](https://www.mysql.com), including configuration for test and non-test environments.
</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [MySQL](#mysql)

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
# MySQL
kubectl port-forward service/mysql 3306:3306 -n dev

# MySQL Workbench
kubectl port-forward service/mysql-workbench 3000:3000 -n dev
kubectl port-forward service/mysql-workbench 3001:3001 -n dev

# Adminer
kubectl port-forward service/adminer 8080:8080 -n dev

# phpMyAdmin
kubectl port-forward service/phpmyadmin 8081:80 -n dev
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
kubectl delete persistentvolumeclaim mysql-pvc -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* MySQL Workbench: [http://localhost:3000](http://localhost:3000)
* Adminer: [http://localhost:8080](http://localhost:8080)
* phpMyAdmin: [http://localhost:8081](http://localhost:8081)

---

## MySQL

### URL

The URL follows the syntax below:

```yaml
url: jdbc:mysql://host:port/database-name
```

There are a few parameters included in the connection string of MySQL as follows:

* useUnicode=true
* useJDBCCompliantTimezoneShift=true
* useLegacyDatetimeCode=false
* serverTimezone=UTC
* pinGlobalTxToPhysicalConnection=TRUE

### Service Commands

After installing MySQL, you can work with the MySQL service from the command line.

Use `mysql -u root -p -h localhost` to connect to the MySQL database in `localhost` (you will need the password for user
`root`).

```shell
# try to connect
mysql -u root -p -h hostname

# import database
mysql -u root -p < file.sql
# import data
mysql -u root -p db-name < file.sql

# backup database
mysqldump mysql -u root -p [–no-data] db-name [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] -databases db-name1 db-name2 ... [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] all-databases [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] db-name table-name1 table-name2 ... [–no-create-info] > file.sql
```

### SQL Commands

A few SQL commands to create schema (database), user, and grant privileges to the user.

```mysql
# schema (database)
SHOW DATABASES;
CREATE DATABASE IF NOT EXISTS 'dbname' DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE dbname;
DROP DATABASE dbname;
SHOW TABLES;

# user
CREATE USER IF NOT EXISTS 'user'@'hostname' IDENTIFIED BY 'password';

# grant
GRANT privilege ON dbname.tablename TO 'user'@'hostname';
FLUSH PRIVILEGES;

# example, assign grant to a user for executing XA transactions 
GRANT BINLOG_ADMIN, SYSTEM_VARIABLES_ADMIN ON *.* TO 'user'@'%';
GRANT XA_RECOVER_ADMIN ON *.* TO 'user'@'%';
GRANT ALL ON `test_db`.* TO 'user'@'%';
FLUSH PRIVILEGES;
```

<p align="justify">
For more information about MySQL see the [MySQL](https://www.mysql.com).
</p>

**<p align="center"> [Top](#integration-of-spring-boot-and-mysql) </p>**