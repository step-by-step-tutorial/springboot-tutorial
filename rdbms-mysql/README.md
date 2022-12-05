# <p align="center">RDBMS MySQL</p>

<p align="justify">


</p>

## How To Use in Spring Boot

### dependency

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
    <groupId>ch.vorburger.mariaDB4j</groupId>
    <artifactId>mariaDB4j-springboot</artifactId>
    <version>2.6.0</version>
    <scope>test</scope>
</dependency>
```

### Spring Boot Configuration

```yaml
spring:
  datasource:
    username: root
    password: password
    url: jdbc:mysql:${MYSQL_HOST:localhost}:3306/springboot_tutorial
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: MySQL
    database-platform: org.hibernate.dialect.MySQL5Dialect
    defer-datasource-initialization: true
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL5Dialect
        generate_statistics: true
        format_sql: true
        naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
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