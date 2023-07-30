# <p align="center">RDBMS PostgreSQL</p>

<p align="justify">

This tutorial is included [PostgreSQL database](https://www.postgresql.org/) configuration for test and none test
environment.

</p>

### URL

```yaml
url: jdbc:postgresql://host:port/database-name
```

## How To

```shell
# first connection
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

## Install PostgreSQL on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

```yaml
version: "3.8"

services:
  postgresql:
    image: postgres:13.9-alpine
    container_name: postgresql
    hostname: postgresql
    restart: always
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: test_db
      PGDATA: /data/postgres
    volumes:
      - "./docker/postgresql:/data/postgres"
  pgadmin:
    image: dpage/pgadmin4
    container_name: pgadmin
    hostname: pgadmin
    restart: always
    ports:
      - "8080:80"
    environment:
      PGADMIN_DEFAULT_EMAIL: pgadmin4@pgadmin.org
      PGADMIN_DEFAULT_PASSWORD: "password"
      PGADMIN_CONFIG_SERVER_MODE: "False"
    volumes:
      - "./docker/pgadmin:/var/lib/pgadmin"
```

Execute the `docker compose  up -d` command to install PostgreSQL and pgadmin.

<p align="justify">

In order to connect to PostgreSQL via Pgadmin open [http://localhost:8080](http://localhost:8080/) through web browser
and use the following properties in the add-server popup.

</p>

```yaml
hostname: postgresql
port: 5432
Username: user
Password: password
```

### Adminer

Also, there is another alternative for Pgadmin for developing SQL named Adminer.

```yaml
adminer:
  image: adminer
  container_name: adminer
  hostname: adminer
  restart: always
  ports:
    - "8080:8080"
```

<p align="justify">

In order to connect to PostgreSQL via Adminer open [http://localhost:8080](http://localhost:8080/) through web browser
and use the following properties in the login page.

</p>

```yaml
System: PostgreSQL
Server: postgresql:5432
Username: user
Password: password
Database: test_db
```

## How To Config Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```

### Test Dependency

```xml

<project>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.testcontainers</groupId>
                <artifactId>testcontainers-bom</artifactId>
                <version>1.18.3</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### Spring Boot Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_NAME:user}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:postgresql://${POSTGRESQL_HOST:localhost}:${POSTGRESQL_PORT:5432}/${DATABASE_NAME:test_db}
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
  config:
    activate:
      on-profile: test
  data:
    jpa:
      hibernate:
        ddl-auto: create
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

##

**<p align="center"> [Top](#rdbms-postgresql) </p>**