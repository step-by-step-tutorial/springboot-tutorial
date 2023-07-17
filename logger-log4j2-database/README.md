# <p align="center">Apache Log4j2 Relational Database</p>

<p>

Log4j is a log framework. For more information
see [https://logging.apache.org/log4j/2.x/](https://logging.apache.org/log4j/2.x/).

</p>

## How To Config Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>
</dependencies>
```

### Spring Boot Properties

Add a bundle named `log4j_en.properties`.

```properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/test_db
username=user
password=password
table_name=LOG_TABLE
```

Add `log4j2.xml` to the resources.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Property>
        <Property name="LOG_LEVEL">INFO</Property>
    </Properties>
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>
        <JDBC name="databaseAppender" tableName="${bundle:log4j:table_name}" bufferSize="1" ignoreExceptions="false">
            <DriverManager
                    driverClassName="${bundle:log4j:driver}"
                    connectionString="${bundle:log4j:url}"
                    userName="${bundle:log4j:username}"
                    password="${bundle:log4j:password}"/>
            <Column name="EVENT_DATE" isEventTimestamp="true"/>
            <Column name="LEVEL" pattern="%level"/>
            <Column name="LOGGER" pattern="%logger"/>
            <Column name="MESSAGE" pattern="%message"/>
        </JDBC>
    </Appenders>
    <Loggers>
        <Root level="${LOG_LEVEL}">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="databaseAppender"/>
        </Root>
        <Logger name="com.tutorial.springboot" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="databaseAppender"/>
        </Logger>
    </Loggers>
</Configuration>

```

## Set up Database

Install a relational database such as MySQL then create new user and execute the following DDL to create a table.

```iso92-sql
CREATE TABLE LOG_TABLE (
  ID INT PRIMARY KEY AUTO_INCREMENT,
  EVENT_DATE TIMESTAMP,
  LEVEL VARCHAR(10),
  LOGGER VARCHAR(255),
  MESSAGE VARCHAR(4000)
);
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

**<p align="center"> [Top](#apache-log4j2-relational-database) </p>**