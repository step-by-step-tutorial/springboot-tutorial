# <p align="center">Integration of Spring boot with ELK Stack</p>

<p align="justify">

This tutorial is about integration of Spring Boot and ELK (Elasticsearch, Logstash, Kibana).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [ELK](#elk)
* [Dockerized](#dockerized)
* [Cloud-Native](#cloud-native)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

### Build

```shell
mvn clean package -DskipTests=true 
```

### Test

```shell
mvn test
```

### Run

```shell
mvn  spring-boot:run
```

### Pipeline

```shell
make LocalPipeline
```

```shell
make DockerizedPipeline
```

```shell
make E2eTest
```

### Down

```shell
docker compose --file ./docker-compose.yml --project-name dev-env down
```

## ELK

<p align="justify">

For more information about ELK see the [https://www.elastic.co](https://www.elastic.co).

</p>

### Use Cases

* Log Management and Analysis
* Security and Threat Detection
* Business Analytics
* IT Infrastructure Monitoring
* Observability and DevOps
* Compliance and Auditing

## Dockerized

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

In this tutorial, I'm using OSS version of ELK stack, therefore it doesn't need to set up securities stuff.
[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: "3.8"
name: ELK
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch-oss:7.10.2
    container_name: elasticsearch
    hostname: elasticsearch
    environment:
      # ELASTIC_PASSWORD: password
      discovery.type: single-node
    ports:
      - "9200:9200"
      - "9300:9300"
    healthcheck:
      test: [ "CMD-SHELL", "curl -sS http://localhost:9200/_cluster/health || exit 1" ]
      interval: 30s
      timeout: 10s
      retries: 3
  logstash:
    image: docker.elastic.co/logstash/logstash:7.10.2
    container_name: logstash
    hostname: logstash
    environment:
      ELASTICSEARCH_URL: http://elasticsearch:9200
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
      # ELASTICSEARCH_USERNAME: logstash_system
      # ELASTICSEARCH_PASSWORD: password
    ports:
      - "5044:5044"
      - "9600:9600"
    volumes:
      - "./elk/logstash/logstash.conf:/usr/share/logstash/pipeline/logstash.conf"
    depends_on:
      - elasticsearch
  kibana:
    image: docker.elastic.co/kibana/kibana-oss:7.10.2
    container_name: kibana
    hostname: kibana
    environment:
      ELASTICSEARCH_URL: http://elasticsearch:9200
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
      # ELASTICSEARCH_USERNAME: kibana_system
      # ELASTICSEARCH_PASSWORD: password
    ports:
      - "5601:5601"
    volumes:
      - "./elk/kibana/kibana.yml:/usr/share/kibana/config/kibana.yml"
    depends_on:
      - elasticsearch
```

### Deploy

Execute the following command to install ELK.

```shell
docker compose --file ./docker-compose.yml --project-name dev-env up --build -d
```

### Down

Execute the following command to stop and remove ELK.

```shell
docker compose --file ./docker-compose.yml --project-name dev-env down
```

### Elasticsearch Check Status

```shell
curl -u elastic:password http://localhost:9200
```

Open [http://localhost:9200](http://localhost:9200) in browser then put the following information.

```yaml
Username: elastic
Password: password
```

#### kibana_system

```shell
# configure the Kibana password in the elasticsearch container
docker exec -it elasticsearch ./bin/elasticsearch-reset-password -u kibana_system -i
```

```shell
# configure the Kibana password via rest API
curl -u elastic:password -X POST -H "Content-Type: application/json" http://localhost:9200/_security/user/kibana_system/_password --data "{\"password\" : \"password\"}"
```

```shell
curl -u kibana_system:password http://localhost:9200/_security/_authenticate
```

#### logstash_system

```shell
# configure the Kibana password in the elasticsearch container
docker exec -it elasticsearch ./bin/elasticsearch-reset-password -u logstash_system -i
```

```shell
# configure the Kibana password via rest API
curl -u elastic:password -X POST -H "Content-Type: application/json" http://localhost:9200/_security/user/logstash_system/_password --data "{\"password\" : \"password\"}"
```

```shell
curl -u logstash_system:password http://localhost:9200/_security/_authenticate
```

#### New User

```shell
curl -X POST -u elastic:password  -H "Content-Type: application/json" localhost:9200/_security/user/kibana_user --data "{\"password\" : \"password\",\"roles\" : [ \"kibana_admin\", \"kibana_system\" ],\"full_name\" : \"Kibana User\"}"
```

### Kibana

Open [http://localhost:5601](http://localhost:5601) in browser then put the following information.

```yaml
Username: elastic
Password: password
```

Check if Elasticsearch is available from Kibana container.

```shell
docker exec kibana curl -u elastic:password http://elasticsearch:9200
```

Use the following query in **`Dev Tools`** to see the logs.

```kibana
# to see indices
GET /_cat/indices?v
```

```kibana
# to see application logs
GET /logstash-2025.01.16/_search
{
  "query": {
    "match_all": {}
  },
  "from": 33, 
  "size": 50
}

```

### Logstash

Open [http://localhost:9600](http://localhost:9600) in browser.

Check if Elasticsearch is available from Logstach container.

```shell
docker exec logstash curl -u elastic:password http://elasticsearch:9200
```

## Cloud-Native

Create the following files for installing ELK.

### Kube Files

[tools_name-deployment.yml](/kube/tools_name-deployment.yml)

```yaml
#deployment.yml
```

[tools_name-service.yml](/kube/tools_name-service.yml)

```yaml
#service.yml
```

### Deploy

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/tools_name-deployment.yml
kubectl apply -f ./kube/tools_name-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to ELK from localhost through the web browser use the following command and dashboard of
ELK is available on [http://localhost:port](http://localhost:port) URL.

</p>

```shell
kubectl port-forward service/tools_name port:port
```

## How To Set up Spring Boot

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
        <Property name="TCP_LOGGER_RECONNECT_DELAY">${env:TCP_LOGGER_RECONNECT_DELAY:-8000}</Property>
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
        <Socket name="SocketAppender" host="${TCP_LOGGER_HOST}" port="${TCP_LOGGER_PORT}">
            <PatternLayout pattern="${LOG_PATTERN}"/>
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

## How To Set up Spring Boot Test

The ELK stack is not designed for a unit testing environment; therefore, a good solution for testing is to perform
End-to-End tests to verify if the log aggregation mechanism works correctly.

## Appendix

### Makefile

```makefile
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file docker-compose.yml --project-name elk up --build -d

DockerComposeDown:
	docker compose --file docker-compose.yml --project-name elk down

DockerRemoveImage:
	docker image rm samanalishiri/application:last

LocalPipeline:
	mvn clean package -DskipTests=true
	mvn test
	mvn spring-boot:run

DockerizedPipeline:
	mvn clean package -DskipTests=true
	mvn test
	docker compose --file docker-compose.yml --project-name elk up --build -d

E2eTest:
	curl http://localhost:8080
```

##

**<p align="center"> [Top](#integration-of-spring-boot-with-elk-stack) </p>**