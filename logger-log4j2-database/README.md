# <p align="center">Apache Log4j2 Relational Database</p>

<p align="justify">

Log4j is a log framework and this tutorial shows how should be integrated Spring Boot 3 and Log4j2 to send logs to a
database. For more information see [https://logging.apache.org/log4j/2.x/](https://logging.apache.org/log4j/2.x/).

</p>

## Set up Database

Install a relational database such as MySQL then create new user and execute the following DDL to create a table.

```shell
# try to connect to mysql
mysql -u root -p  -h hostname
```

```iso92-sql
# user
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';

# table
CREATE TABLE LOG_TABLE (
  ID INT PRIMARY KEY AUTO_INCREMENT,
  EVENT_DATE TIMESTAMP,
  LEVEL VARCHAR(10),
  LOGGER VARCHAR(255),
  MESSAGE VARCHAR(4000)
);
```

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
    <!--append your database driver-->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
</dependencies>
```

### Spring Boot Properties

Create a bundle named `log4j2_en.properties` include the following properties in the resources. In this case I am using
MySQL properties.

```properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/test_db
username=user
password=password
table_name=LOG_TABLE
```

Create `log4j2.xml` in the resources with proper configuration for the database.

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
        <JDBC name="databaseAppender" tableName="${bundle:log4j2:table_name}" bufferSize="1" ignoreExceptions="false">
            <DriverManager
                    driverClassName="${bundle:log4j2:driver}"
                    connectionString="${bundle:log4j2:url}"
                    userName="${bundle:log4j2:username}"
                    password="${bundle:log4j2:password}"/>
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
        <Logger name="package-name" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="databaseAppender"/>
        </Logger>
    </Loggers>
</Configuration>
```

## Prerequisites

* [Java 21](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)

## Build

```bash
mvn clean package -DskipTests=true 
```

## Test

If the MySql instance is ready and already configured then it is possible to run the unit tests.

```bash
mvn test "-Dmysql.isReady=true"
```

## Run

```bash
mvn  spring-boot:run
```

##

**<p align="center"> [Top](#apache-log4j2-relational-database) </p>**