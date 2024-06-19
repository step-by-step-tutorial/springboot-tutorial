# <p align="center">RDBMS H2</p>

<p align="justify">

H2 is a light relational database implemented by java to use for embedded, in-memory and server mode. It is proper to
use it as a test database for testing or cache for an application. Also, it is able to work with file instead of memory.
For more information about H2 go to the [website of H2](https://www.h2database.com/html/main.html).

</p>

## How To Config Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

#### Web Console

In order to access to the ht web console the application should be included the following dependency.

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Spring Boot Configuration

```yaml

spring:
  main:
    banner-mode: OFF
  datasource:
    username: sa
    password: ''
    url: jdbc:h2:mem:${DATABASE_NAME:test_db}
    driver-class-name: org.h2.Driver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: H2
    database-platform: org.hibernate.dialect.H2Dialect
    defer-datasource-initialization: true
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        generate_statistics: true
        format_sql: true
        naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
  h2:
    console:
      enabled: true
      path: ${H2_CONSOLE:/h2-console}
```

## Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)

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

After running, see H2 web console [localhost:8080/h2-console](localhost:8080/h2-console).

##

**<p align="center"> [Top](#rdbms-h2) </p>**