# <p align="center"> Integration of Spring Boot And Apache Log4j2 With Relational Database Appender</p>

<p style="text-align: justify;">

Log4j is a log framework, and this tutorial shows how it should be integrated with Spring Boot 3 and Log4j2 to send logs
to a
database. For more information see [https://logging.apache.org/log4j/2.x](https://logging.apache.org/log4j/2.x).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [Apache Log4j2](#apache-log4j2)

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
docker exec -i mysql mysql -h localhost -u user -ppassword -e "USE tutorial_db; SELECT * FROM LOG_TABLE;"
```

### Stop

```shell
mvn  spring-boot:stop
```

```shell
docker exec -i mysql mysql -h localhost -u user -ppassword -e "USE tutorial_db; TRUNCATE TABLE LOG_TABLE;"
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
docker exec -i mysql mysql -h localhost -u user -ppassword -e "USE tutorial_db; SELECT * FROM LOG_TABLE;"
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
kubectl exec -it $POD_FULL_NAME  -n dev -c mysql -- mysql -u user -ppassword -h localhost -e "USE tutorial_db; SELECT * FROM LOG_TABLE;"
```

### Port Forwarding

```shell
kubectl port-forward service/mysql 3306:3306 -n dev
```

```shell
kubectl port-forward service/adminer 8081:8081 -n dev
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

Adminer: [http://localhost:8081](http://localhost:8081)

```yaml
Server: mysql:3306
Username: user
Password: password
```

## Apache Log4j2

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>
    <!--append your database driver-->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
</dependencies>
```

### Apache Log4j2 (log4j2.xml) and Database Appender

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Property>
        <Property name="LOG_LEVEL">INFO</Property>
        <Property name="LOG_TABLE">${env:LOG_TABLE:-LOG_TABLE}</Property>
        <Property name="DATABASE_URL">${env:DATABASE_URL:-jdbc:mysql://localhost:3306/tutorial_db}</Property>
        <Property name="DATABASE_USER">${env:DATABASE_USER:-user}</Property>
        <Property name="DATABASE_PASSWORD">${env:DATABASE_PASSWORD:-password}</Property>
        <Property name="DATABASE_RECONNECT_DELAY">${env:DATABASE_RECONNECT_DELAY:-8000}</Property>

    </Properties>
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <JDBC name="DatabaseAppender" tableName="${LOG_TABLE}" bufferSize="1" ignoreExceptions="true">
            <DriverManager
                    driverClassName="com.mysql.cj.jdbc.Driver"
                    connectionString="${DATABASE_URL}"
                    userName="${DATABASE_USER}"
                    password="${DATABASE_PASSWORD}"
            />
            <ReconnectIntervalMillis>${DATABASE_RECONNECT_DELAY}</ReconnectIntervalMillis>
            <Column name="EVENT_DATE" isEventTimestamp="true"/>
            <Column name="LEVEL" pattern="%level"/>
            <Column name="LOGGER" pattern="%logger"/>
            <Column name="MESSAGE" pattern="%message"/>
        </JDBC>

    </Appenders>
    <Loggers>
        <Root level="${LOG_LEVEL}">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="DatabaseAppender"/>
        </Root>
        <Logger name="com.tutorial.springboot" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="DatabaseAppender"/>
        </Logger>
    </Loggers>
</Configuration>

```

### Init MySQL

This session is already done in deployment time. Therefore, the description is only for more information.

<p>

After installation a MySQL database, create a new user, database and create the `LOG_TABLE` table. This step applies
automatically in this tutorial.

</p>

Connect to MySQL with the following command.

```shell
# try to connect to docker container
docker exec -it mysql mysql -h hostname -u user -p

# Example
#   user:     user
#   password: password 
#   hostname: localhost
docker exec -it mysql mysql -h localhost -u user -p
```

Execute following queries.

```mysql
# init.db
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';
CREATE DATABASE IF NOT EXISTS tutorial_db;
USE tutorial_db;

CREATE TABLE LOG_TABLE
(
    ID         INT PRIMARY KEY AUTO_INCREMENT,
    EVENT_DATE TIMESTAMP,
    LEVEL      VARCHAR(10),
    LOGGER     VARCHAR(255),
    MESSAGE    VARCHAR(4000)
);
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-log4j2-with-relational-database-appender) </p>**
