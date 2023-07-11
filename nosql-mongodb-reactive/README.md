# <p align="center">NoSQL Reactive MongoDB</p>

<p align="justify">

This tutorial is included [MongoDB](https://www.mongodb.com/) configuration for test and none test environment.

</p>

## Install MongoDB on Docker

Execute the `docker compose  up -d` command to install MongoDB.

```yaml
version: '3.8'

services:

  mongo:
    image: mongo
    container_name: mongo
    hostname: mongo
    restart: always
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: root

  mongo-express:
    image: mongo-express
    container_name: mongo-express
    hostname: mongo-express
    restart: always
    ports:
      - "8081:8081"
    environment:
      ME_CONFIG_MONGODB_ADMINUSERNAME: root
      ME_CONFIG_MONGODB_ADMINPASSWORD: root
      ME_CONFIG_MONGODB_URL: mongodb://root:root@mongo:27017
```

## How To Config Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
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
    <artifactId>mongodb</artifactId>
    <scope>test</scope>
</dependency>
</dependencies>
```

### Spring Boot Properties

```yaml
spring:
  data:
    mongodb:
      username: root
      password: root
      host: ${MONGO_DB_HOST:localhost}
      port: ${MONGO_DB_PORT:27017}
      database: ${MONGO_DB_NAME:springboot_tutorial}
      authentication-database: admin
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
mvn test
```

## Run

```bash
mvn  spring-boot:run
```

**<p align="center"> [Top](#nosql-reactive-mongodb) </p>**