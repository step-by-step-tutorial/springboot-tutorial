# <p align="center">Integration of Spring Boot And Apache Log4j2 With File</p>

<p align="justify">

Log4j is a log framework and this tutorial shows how should be integrated Spring Boot 3 and Log4j2 to append logs to the
file. For more information see [https://logging.apache.org/log4j/2.x](https://logging.apache.org/log4j/2.x).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Apache Log4j2](#apache-log4j2)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)

### Build

```shell
mvn validate clean compile 
```

### Test

```shell
mvn test
```

### Package

```shell
mvn package -DskipTests=true
```

### Run

```shell
mvn  spring-boot:start
```

### Stop

```shell
mvn spring-boot:stop
```

## Apache Log4j2

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

### Apache Log4j2 (log4j2.xml) and File Appender

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_PATH">./target/logs/</Property>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Property>
        <Property name="LOG_LEVEL">INFO</Property>
    </Properties>
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>
        <RollingFile name="FileAppender"
                     fileName="${LOG_PATH}/application.log"
                     filePattern="${LOG_PATH}/application-%d{yyyy-MM-dd}.log.gz">
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
    </Appenders>
    <Loggers>
        <Root level="${LOG_LEVEL}">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="FileAppender"/>
        </Root>
        <Logger name="com.tutorial.springboot" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="FileAppender"/>
        </Logger>
    </Loggers>
</Configuration>
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-log4j2-with-file) </p>**
