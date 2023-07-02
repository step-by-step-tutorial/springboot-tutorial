# <p align="center">RDBMS Oracle</p>

<p align="justify">

This tutorial is included [Oracle database](https://www.oracle.com/) configuration for test and none test environment.

</p>

**URL Example**

```yaml
url: jdbc:oracle:thin:${ORACLE_HOST:localhost}:${ORACLE_PORT:1521}/${DATABASE_NAME:xepdb1}
```

## Install Oracle on Docker

<p align="justify">

There is no official docker image on docker hub, therefore you should use
the [GitHub repository](https://github.com/oracle/docker-images/tree/main/OracleDatabase/SingleInstance) to install
Oracle database on docker.

</p>
Example of how to install Oracle database XE edition.

```text
step-1: download oracle-database-xe-21c-1.0-1.ol8.x86_64.rpm from https://www.oracle.com/database/technologies/xe-downloads.html
step-2: git clone https://github.com/oracle/docker-images.git
step-3: copy oracle-database-xe-21c-1.0-1.ol8.x86_64.rpm to path-to/OracleDatabase/SingleInstance/dockerfiles/21.3.0/
step-4: cd path-to/OracleDatabase/SingleInstance/dockerfiles
step-5: buildDockerImage.sh -x -v 21.3.0
step-6: docker run --name oracle-xe-21c \
-p 1521:1521 -p 5500:5500 \
-e ORACLE_PWD=Adminxe18c \
-e ORACLE_CHARACTERSET=UTF8 \
-v /opt/oracle/oradata \
oracle/database:21.3.0-xe

step-7: docker stop/start oracle-xe-21c
```

```yaml
version: "3.8"

services:
  oracle:
    #container-registry.oracle.com/database/free:latest
    image: container-registry.oracle.com/database/express:21.3.0-xe
    container_name: oracle
    hostname: oracle
    restart: always
    ports:
      - "1521:1521"
      - "5500:5500"
    environment:
      ORACLE_PWD: password
      ORACLE_CHARACTERSET: utf-8
```

### Enterprise Manager

Open https://localhost:5500/em via web browser.

* user: system
* password: password
* container name: xepdb1

## How To Config Spring Boot

### Dependency

The Oracle database driver should
be [downloaded](https://www.oracle.com/de/database/technologies/appdev/jdbc-downloads.html) and install manually with
following command.

**Linux/Unix**

```shell
#!/usr/bin/env bash

mvn install:install-file \
-Dfile=path/ojdbc11.jar \
-DgroupId=com.oracle \
-DartifactId=ojdbc11 \
-Dversion=21c \
-Dpackaging=jar
```

**Windows**

```shell
mvn install:install-file ^
-Dfile=./ojdbc11.jar ^
-DgroupId=com.oracle ^
-DartifactId=ojdbc11 ^
-Dversion=21c ^
-Dpackaging=jar ^
-DgeneratePom=true

```

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.oracle</groupId>
        <artifactId>ojdbc11</artifactId>
        <version>21c</version>
    </dependency>
</dependencies>
```

### Test Dependency

```xml

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
    <artifactId>oracle-xe</artifactId>
    <scope>test</scope>
</dependency>
</dependencies>
```

### Spring Boot Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_USERNAME:system}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:oracle:thin:${ORACLE_HOST:localhost}:${ORACLE_PORT:1521}/${DATABASE_NAME:xepdb1}
    driver-class-name: oracle.jdbc.driver.OracleDriver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: ORACLE
    database-platform: org.hibernate.dialect.OracleDialect
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
        default_schema: ${spring.datasource.username}
---
spring:
  profiles:
    active:
      - test
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

**<p align="center"> [Top](#rdbms-oracle) </p>**