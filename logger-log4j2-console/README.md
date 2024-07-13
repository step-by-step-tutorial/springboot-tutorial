
# <p align="center">Apache Log4j2 And Console Appender</p>

<p align="justify">

Log4j is a log framework and this tutorial shows how should be integrated Spring Boot 3 and Log4j2 to print logs on the
console. For more information see [https://logging.apache.org/log4j/2.x](https://logging.apache.org/log4j/2.x).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Log Level](#log-level)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)

### Pipeline

#### Build

```bash
mvn clean package -DskipTests=true 
```

#### Test

```bash
mvn test
```

#### Run

```bash
mvn  spring-boot:run
```

## Log Level

To filter log messages according to their importance or severity.

```text
 If log level is OFF    =>  log nothing
 If log level is ERROR  =>  log errors
 If log level is WARN   =>  log errors, warns
 If log level is INFO   =>  log errors, warns, info
 If log level is DEBUG  =>  log errors, warns, info, debug 
 If log level is TRACE  =>  log errors, warns, info, debug, trace 
 If log level is ALL    =>  log everything such as errors, warns, etc
```

## How To Set up Spring Boot

### Dependencies

The default logger must be excluded.

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

### Log4j Properties

Add `log4j2.xml` to the **resources** directory.

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
    </Appenders>
    <Loggers>
        <Root level="${LOG_LEVEL}">
            <AppenderRef ref="ConsoleAppender"/>
        </Root>
        <Logger name="package-name" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
        </Logger>
    </Loggers>
</Configuration>
```

##

**<p align="center"> [Top](#apache-log4j2-console) </p>**
