# <p align="center">Apache Log4j2</p>

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

Add log4j2.yml to the resources.

```yaml
Configuration:
  name: Default
  appenders:
    Console:
      name: LogToConsole
      PatternLayout:
        Pattern: "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"
  Loggers:
    logger:
      - name: com.tutorial.springboot.logger_log4j2
        level: debug
        additivity: false
        AppenderRef:
          - ref: LogToConsole
  Root:
    level: error
    AppenderRef:
      ref: LogToConsole
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

**<p align="center"> [Top](#apache-log4j2) </p>**