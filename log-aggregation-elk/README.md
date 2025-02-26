# <p align="center">Integration of Spring boot with ELK Stack</p>

<p align="justify">

This tutorial is about integration of Spring Boot and ELK (Elasticsearch, Logstash, Kibana). For more information about
ELK see the [https://www.elastic.co](https://www.elastic.co).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [Application Config](#application-config)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

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

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Stop

```shell
mvn  spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
docker volume prune -f
```

## Dockerized

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker compose --file ./docker-compose.yml --project-name dev up --build -d
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

```kql
GET /_cat/indices?v

GET /logstash-2025.02.26/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0, 
  "size": 50
}
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev down
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## Kubernetes

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker build -t samanalishiri/application:latest . --no-cache
```

```shell
kubectl apply -f kube-dev.yml
```

### Check Status

```shell
kubectl get all -n dev
```

### Port-Forwarding

```shell
kubectl port-forward service/elasticsearch 9200:9200 -n dev
```

```shell
kubectl port-forward service/kibana 5601:5601 -n dev
```

```shell
kubectl port-forward service/application 8080:8080 -n dev
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
kubectl delete all --all -n dev
kubectl delete secrets dev-credentials -n dev
kubectl delete configMap dev-config -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* Elasticsearch: [http://localhost:9200](http://localhost:9200)
* Kibana: [http://localhost:5601](http://localhost:5601)
* Application: [http://localhost:8080](http://localhost:8080)

## Application Config

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

### Apache Log4j2

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Property>
        <Property name="LOG_LEVEL">INFO</Property>
        <Property name="LOG_PATH">./target/logs/</Property>
        <Property name="TCP_LOGGER_HOST">${env:TCP_LOGGER_HOST:-localhost}</Property>
        <Property name="TCP_LOGGER_PORT">${env:TCP_LOGGER_PORT:-5044}</Property>
        <Property name="TCP_LOGGER_RECONNECT_DELAY">${env:TCP_LOGGER_RECONNECT_DELAY:-5000}</Property>
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
        <Socket name="SocketAppender" host="${TCP_LOGGER_HOST}" port="${TCP_LOGGER_PORT}" bufferSize="1"
                ignoreExceptions="true">
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <ConnectTimeoutMillis>${TCP_LOGGER_RECONNECT_DELAY}</ConnectTimeoutMillis>
            <ReconnectDelayMillis>${TCP_LOGGER_RECONNECT_DELAY}</ReconnectDelayMillis>
        </Socket>
    </Appenders>
    <Loggers>
        <Root level="${LOG_LEVEL}">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="FileAppender"/>
            <AppenderRef ref="SocketAppender"/>
        </Root>
        <Logger name="com.tutorial.springboot" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="FileAppender"/>
            <AppenderRef ref="SocketAppender"/>
        </Logger>
    </Loggers>
</Configuration>
```

### Kibana

```yaml
server.host: "0.0.0.0"
elasticsearch.hosts: [ "http://elasticsearch:9200" ]
```

### Logstash

```conf
input {
  tcp {
      port => 5044
      codec => json_lines
    }
}

output {
 stdout {
    codec => rubydebug
 }
 elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "logstash_system"
    password => "password"
 }
}

```

##

**<p align="center"> [Top](#integration-of-spring-boot-with-elk-stack) </p>**