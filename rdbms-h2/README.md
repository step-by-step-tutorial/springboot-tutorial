# <p align="center">RDBMS H2</p>

<p align="justify">

H2 is a light relational database implemented by java to use for embedded, in-memory and server mode. It is proper to
use as a test database or cache for application. Also, it is able to work with file instead of memory. For more
information about H2 go to the [website of H2](https://www.h2database.com/html/main.html).

</p>

<p align="justify">

There are a few alternatives for H2, therefore you can read the
[article](https://github.com/oss-academy/article/blob/main/java-in-memory-db.md#Java-In-Memory-Database).

</p>

## How To Use in Spring Boot

### dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Spring Boot Configuration

```yaml
spring:
  datasource:
    username: sa
    password: ''
    url: jdbc:h2:mem:rdbms_h2
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
        dialect: org.hibernate.dialect.H2Dialect
        generate_statistics: true
        format_sql: true
        naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
  h2:
    console:
      enabled: true
      path: "/h2-console"
```

## Prerequisites

* Java 17
* Maven 3

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

After running see H2 web console [localhost:8080/h2-console](localhost:8080/h2-console).

**<p align="center"> [Top](#RDBMS-H2) </p>**