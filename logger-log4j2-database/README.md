
# <p align="center">Apache Log4j2 And Relational Database Appender</p>

<p align="justify">

Log4j is a log framework and this tutorial shows how should be integrated Spring Boot 3 and Log4j2 to send logs to a
database. For more information see [https://logging.apache.org/log4j/2.x](https://logging.apache.org/log4j/2.x).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Set up Database](#set-up-database)
    * [Install Dockerized MySQL](#install-dockerized-mysql)
    * [Initialize MySQL Manually](#initialize-mysql-manually)
* [How To Config Application](#how-to-config-application)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [MySQL](https://www.mysql.com)
* [Docker](https://www.docker.com)

## Set up Database

### Install Dockerized MySQL

Create a docker compose file named `docker-compose.yml` then copy and paste the following script. There are two clients
to help you for interacting with MySQL therefore you can select one of
them, [MySQL workbench](https://www.mysql.com/products/workbench) or [Adminer](https://www.adminer.org/). If you install
MySQL via docker compose script mentioned below then database will be initialized and configured and, you don't need to
initialize it manually.

```yaml
version: "3.8"

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
      - MYSQL_DATABASE=test_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./target/mysql:/etc/mysql/conf.d"
      - "./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql"
  mysql-workbench:
    image: linuxserver/mysql-workbench:latest
    container_name: mysql-workbench
    hostname: mysql-workbench
    restart: unless-stopped
    environment:
      - PUID=1000
      - PGID=1000
      - TZ=Etc/UTC
    volumes:
      - ./target:/config
    ports:
      - "3000:3000"
      - "3001:3001"
    cap_add:
      - IPC_LOCK
  adminer:
    image: adminer
    container_name: adminer
    hostname: adminer
    restart: always
    ports:
      - "8080:8080"
```

### Initialize MySQL Manually

After installation a MySQL database, create new user, database and create the `LOG_TABLE` table.

```shell
# try to connect to docker container
docker exec -it mysql sh
```

```shell
# try to connect to mysql via MySQL client
# user: root
# password: root 
# hostname: localhost
mysql -h hostname -u root -p
```

```mysql
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';
CREATE DATABASE IF NOT EXISTS test_db;
USE test_db;

CREATE TABLE LOG_TABLE
(
    ID         INT PRIMARY KEY AUTO_INCREMENT,
    EVENT_DATE TIMESTAMP,
    LEVEL      VARCHAR(10),
    LOGGER     VARCHAR(255),
    MESSAGE    VARCHAR(4000)
);
```

### Pipeline

#### Build

```bash
mvn clean package -DskipTests=true 
```

#### Test

In this tutorial I don't want to use [Testcontainers](https://testcontainers.com) framework to test because I want to
check database after inserting log. If the MySql instance is ready and already configured then it is possible to run the
unit tests.

```bash
mvn test "-Dmysql.isReady=true"
```

After running tests successfully then connect to MySQL database and execute the following queries to see the result.

```mysql
show databases;
use test_db;
show tables;

SELECT *
FROM LOG_TABLE;
```

#### Run

```bash
mvn  spring-boot:run
```

## How To Config Application

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

### Log4j Properties

Create a bundle named `log4j2_en.properties` include the following properties in the resources. In this case I am using
MySQL properties.

```properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/test_db
table_name=LOG_TABLE
username=user
password=password
```

Create `log4j2.xml` in the resources with proper configuration for the database.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Property>
        <Property name="LOG_LEVEL">INFO</Property>
    </Properties>
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>
        <JDBC name="databaseAppender" tableName="${bundle:log4j2:table_name}" bufferSize="1" ignoreExceptions="false">
            <DriverManager
                    driverClassName="${bundle:log4j2:driver}"
                    connectionString="${bundle:log4j2:url}"
                    userName="${bundle:log4j2:username}"
                    password="${bundle:log4j2:password}"/>
            <Column name="EVENT_DATE" isEventTimestamp="true"/>
            <Column name="LEVEL" pattern="%level"/>
            <Column name="LOGGER" pattern="%logger"/>
            <Column name="MESSAGE" pattern="%message"/>
        </JDBC>
    </Appenders>
    <Loggers>
        <Root level="${LOG_LEVEL}">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="databaseAppender"/>
        </Root>
        <Logger name="package-name" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="databaseAppender"/>
        </Logger>
    </Loggers>
</Configuration>
```

##

**<p align="center"> [Top](#apache-log4j2-relational-database) </p>**
