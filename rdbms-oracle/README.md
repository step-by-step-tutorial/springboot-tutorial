# <p align="center">RDBMS MySQL</p>

<p align="justify">

This tutorial is included [mysql](https://www.mysql.com/) configuration for test and none test environment.

Some parameters can be included in mysql URL connection are as follows.

* useUnicode=true
* useJDBCCompliantTimezoneShift=true
* useLegacyDatetimeCode=false
* serverTimezone=UTC
* pinGlobalTxToPhysicalConnection=TRUE

</p>

**URL Example**

```yaml
url: jdbc:mysql://${MYSQL_HOST}/${MYSQL_DATABASE}?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&pinGlobalTxToPhysicalConnection=TRUE
```

## Install MySQL on Docker

Execute the `docker compose  up -d` command to install MySQL and a SQL developer named adminer.

```yaml
version: "3.8"

services:
  mysql:
    container_name: mysql
    hostname: mysql
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      - MYSQL_USER=user
      - MYSQL_PASSWORD=password
      - MYSQL_DATABASE=springboot_tutorial
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./docker/mysql:/etc/mysql/conf.d"
  adminer:
    container_name: adminer
    hostname: adminer
    image: adminer
    restart: always
    ports:
      - "8080:8080"
```
In order to connect to MySQL via adminer brows [http://localhost:8080](http://localhost:8080/) via web browser and use 
the following properties in the login page.

```yaml
System: MySQL
Server: mysql:3306
Username: user
Password: password
Database: springboot_tutorial
```

## How To Config Spring Boot

### Dependency

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>

```

### Spring Boot Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_NAME:user}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:mysql:${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${DATABASE_NAME:springboot_tutorial}
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
        default_schema: ${DATABASE_SCHEMA:sample}
---
spring:
  profiles:
    active:
      - test
  jpa:
    hibernate:
      ddl-auto: create-drop

```

## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)

## Build

```bash

mvn clean package -DskipTests=true
```

## Test

```bash
mvn  test
```

## Run

```bash
mvn  spring-boot:run
```

**<p align="center"> [Top](#RDBMS-MySQL) </p>**