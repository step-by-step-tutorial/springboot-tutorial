# <p align="center">RDBMS Oracle</p>

<p align="justify">

This tutorial is included [Oracle database](https://www.oracle.com/) configuration for test and none test environment.

</p>

**URL Example**

```yaml
url: jdbc:oracle:thin:${ORACLE_HOST:localhost}:${ORACLE_PORT:1521}/${DATABASE_NAME:xepdb1}
```

## Install Oracle on Docker

If you want to install ORDS then you have to create the following directory and connection file.

```shell

mkdir ords_secrets
mkdir ords_config
# linux/unix
echo CONN_STRING="sys as sysdba/password@oracle:1521/xepdb1" > ords_secrets/conn_string.txt
# windows powershell
echo 'CONN_STRING="sys as sysdba/password@oracle:1521/xepdb1"' > ords_secrets/conn_string.txt
# windows cmd
echo CONN_STRING=^"sys as sysdba/password@oracle:1521/xepdb1^" > ords_secrets/conn_string.txt

```

Execute the `docker compose  up -d` command to install Oracle database.

```yaml
version: "3.8"

services:
  oracle:
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
    volumes:
      - "./docker/oracle_data:/opt/oracle/oradata"
  ords:
    image: container-registry.oracle.com/database/ords:latest
    container_name: ords
    hostname: ords
    restart: always
    links:
      - oracle
    ports:
      - "8181:8181"
    volumes:
      - "./ords_secrets/:/opt/oracle/variables/"
      - "./ords_config/:/etc/ords/config/"
```

### Set up Database

Install [SqlPlus](https://www.oracle.com/database/technologies/instant-client/downloads.html) then connect to oracle
db-service by following command and after that create your target user. Use the system or sys user.

```shell
sqlplus system/password@//localhost:1521/xepdb1
```

```oracle-sql
CREATE USER target_user IDENTIFIED BY target_password;
rem grants based on the requirements
```

### Enterprise Manager

Open https://localhost:5500/em in web browser.

* user: system
* password: password
* container name: xepdb1

### ORDS

You have to enable the ORDS schema for the target user. Therefor connect to the db-service by target user and execute
the procedure.

```shell
sqlplus target_user/target_password@//localhost:1521/xepdb1
```

```oracle-plsql
 DECLARE
     PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
 
        ORDS.ENABLE_SCHEMA(p_enabled => TRUE,
                           p_schema => 'TARGET_USER',
                           p_url_mapping_type => 'BASE_PATH',
                           p_url_mapping_pattern => 'target_user',
                           p_auto_rest_auth => FALSE);

      commit;

  END;
/
```

Open http://localhost:8181/ords in web browser, then use target_user and its password to log in to
the `Sql Developer Web`.

## How To Config Spring Boot

### Dependencies

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
            <artifactId>oracle-xe</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
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
mvn  test "-longTimeTest.isActivate=true"
```

## Run

```bash
mvn  spring-boot:run
```

**<p align="center"> [Top](#rdbms-oracle) </p>**