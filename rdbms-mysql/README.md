# <p style="text-align: center;"> Integration of Spring Boot And MySQL</p>

## <p style="text-align: center;"> Table of Content </p>

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
```

```shell
# MySQL Workbench
kubectl port-forward service/mysql-workbench 3000:3000 -n dev
```

```shell
kubectl port-forward service/mysql-workbench 3001:3001 -n dev
```

```shell
# Adminer
kubectl port-forward service/adminer 8082:8080 -n dev
```

```shell
# phpMyAdmin
kubectl port-forward service/phpmyadmin 8081:80 -n dev
```

```shell
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
kubectl delete persistentvolumeclaim mysql-pvc -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* MySQL Workbench: [http://localhost:3000](http://localhost:3000)
* Adminer: [http://localhost:8082](http://localhost:8082)
* phpMyAdmin: [http://localhost:8081](http://localhost:8081)

```yaml
Server: mysql:3306
Username: user
Password: password
Database: tutorial_db
```

---

## MySQL

### URL Format

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
GRANT ALL ON `tutorial_db`.* TO 'user'@'%';
FLUSH PRIVILEGES;
```

<p style="text-align: justify;">
For more information about MySQL see the [MySQL](https://www.mysql.com).
</p>

##

**<p style="text-align: center;"> [Top](#integration-of-spring-boot-and-mysql) </p>**