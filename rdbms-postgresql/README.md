# <p align="center">RDBMS PostgreSQL</p>

<p align="justify">

This tutorial is included [PostgreSQL](https://www.postgresql.org/) configuration for test and none test environment.

</p>

## Install PostgreSQL on Docker

Execute the `docker compose  up -d` command to install PostgreSQL and a SQL developer named adminer and pgadmin.

```yaml
version: "3.8"

services:
  postgresql:
    container_name: postgresql
    hostname: postgresql
    image: postgres:13.9-alpine
    restart: always
    ports:
      - 5432:5432
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: springboot_tutorial
      PGDATA: /data/postgres
    volumes:
      - "./docker/postgresql:/data/postgres"
  pgadmin:
    container_name: pgadmin
    hostname: pgadmin
    image: dpage/pgadmin4
    restart: unless-stopped
    ports:
      - "8081:80"
    environment:
      PGADMIN_DEFAULT_EMAIL: pgadmin4@pgadmin.org
      PGADMIN_DEFAULT_PASSWORD: "password"
      PGADMIN_CONFIG_SERVER_MODE: "False"
    volumes:
      - "./docker/pgadmin:/var/lib/pgadmin"
  adminer:
    image: adminer
    restart: always
    ports:
      - "8080:8080"
```
In order to connect to PostgreSQL via adminer brows [http://localhost:8080](http://localhost:8080/) via web browser and
use the following properties in the login page.

```yaml
System: PostgreSQL
Server: postgresql:5432
Username: user
Password: password
Database: springboot_tutorial
```
In order to connect to PostgreSQL via pgadmin brows [http://localhost:8081](http://localhost:8081/) via web browser and
use the following properties in the add server popup.

```properties
hostname: postgresql
port: 5432
Username: user
Password: password
```

## How To Config Spring Boot

### Dependency

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>

```

### Spring Boot Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_NAME:user}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:postgresql://${POSTGRESQL_HOST:localhost}:${POSTGRESQL_PORT:5432}/${DATABASE_NAME:springboot_tutorial}
    driver-class-name: org.postgresql.Driver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: POSTGRESQL
    database-platform: org.hibernate.dialect.PostgreSQLDialect
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

**<p align="center"> [Top](#RDBMS-PostgreSQL) </p>**