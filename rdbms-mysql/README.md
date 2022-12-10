# <p align="center">RDBMS MySQL</p>

<p align="justify">

This tutorial is included [mysql](https://www.mysql.com/) configuration for test and none test environment.

Some parameters can be included in mysql URL connection are follows.
* useUnicode=true
* useJDBCCompliantTimezoneShift=true
* useLegacyDatetimeCode=false
* serverTimezone=UTC
* pinGlobalTxToPhysicalConnection=TRUE

</p>

**Example**
```yaml
url: jdbc:mysql://${MYSQL_HOST}/${MYSQL_DATABASE}?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&pinGlobalTxToPhysicalConnection=TRUE
```

## How To Use in Spring Boot

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

### Spring Boot Configuration

```yaml
spring:
  datasource:
    username: { $DATABASE_NAME:user }
    password: { $DATABASE_PASSWORD:password }
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

**<p align="center"> [Top](#RDBMS-MySQL) </p>**