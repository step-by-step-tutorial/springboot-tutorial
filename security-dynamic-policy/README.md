# <p align="center">Security Dynamic Policy</p>

<p align="justify">

This tutorial is about implementing dynamic policy by Spring Boot.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dynamic Policy](#dynamic-policy)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)

### Pipeline

#### Build

```shell
mvn clean package -DskipTests=true 
```

#### Test

```shell
mvn test
```

#### Run

```shell
mvn  spring-boot:run
```

## Dynamic Policy

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
</dependencies>
```

### Application Properties

#### Main Profile

```yaml
# application.yml
server:
  address: ${APP_HOST:0.0.0.0}
  port: ${APP_PORT:8080}
spring:
  main:
    banner-mode: OFF
  profiles:
    active: ${APP_PROFILES:h2}
```

#### H2 Profile

```yaml
# application-h2.yml
spring:
  datasource:
    username: ${DATABASE_USERNAME:sa}
    password: ${DATABASE_PASSWORD:}
    url: jdbc:h2:mem:${DATABASE_NAME:tutorial_db}
    driver-class-name: org.h2.Driver
  data:
    jdbc:
      repositories:
        enabled: true
  jpa:
    database: H2
    database-platform: org.hibernate.dialect.H2Dialect
    defer-datasource-initialization: true
    hibernate:
      ddl-auto: create-drop
    properties:
      javax:
        persistence:
          create-database-schemas: false
      hibernate:
        naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
        default_schema: ""
  h2:
    console:
      enabled: true
      path: ${H2_CONSOLE:/h2-console}
  sql:
    init:
      data-locations: classpath:data.sql
```

## How To Set up Spring Boot Test

### Dependencies

```xml

<dependencies>
    <!--test-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
    </dependency>
    <dependency>
        <groupId>net.datafaker</groupId>
        <artifactId>datafaker</artifactId>
        <version>2.3.1</version>
    </dependency>
</dependencies>
```

## Appendix

### Makefile

```shell
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

```

##

**<p align="center"> [Top](#security-dynamic-policy) </p>**