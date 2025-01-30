# <p align="center">Integration of Spring boot with ELK Stack</p>

<p align="justify">

This tutorial is about integration of Spring Boot and ELK (Elasticsearch, Logstash, Kibana).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [ELK](#elk)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)

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
Check the Kibana web console under dev tools.

### Stop

```shell
mvn  spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
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

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
name: dev-env
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch-oss:7.10.2
    container_name: elasticsearch
    hostname: elasticsearch
    environment:
      ELASTIC_PASSWORD: password
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
      ELASTICSEARCH_USERNAME: logstash_system
      ELASTICSEARCH_PASSWORD: password
    ports:
      - "5044:5044"
      - "9600:9600"
    volumes:
      - "./elk/logstash/logstash.conf:/usr/share/logstash/pipeline/logstash.conf"
    depends_on:
      elasticsearch:
        condition: service_healthy
  kibana:
    image: docker.elastic.co/kibana/kibana-oss:7.10.2
    container_name: kibana
    hostname: kibana
    environment:
      ELASTICSEARCH_URL: http://elasticsearch:9200
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
      ELASTICSEARCH_USERNAME: kibana_system
      ELASTICSEARCH_PASSWORD: password
    ports:
      - "5601:5601"
    volumes:
      - "./elk/kibana/kibana.yml:/usr/share/kibana/config/kibana.yml"
    depends_on:
      elasticsearch:
        condition: service_healthy
```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker compose --file docker-compose.yml --project-name dev-env up --build -d
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev-env down
```

## Kubernetes

Create the following files for installing ELK.

### Kube Files

[elasticsearch.yml](/kube/elasticsearch.yml)

```yaml
---
apiVersion: v1
kind: Secret
metadata:
  name: elasticsearch-credentials
  labels:
    app: elasticsearch
type: Opaque
data:
  # password
  ELASTIC_PASSWORD: cGFzc3dvcmQ=
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: elasticsearch
spec:
  replicas: 1
  selector:
    matchLabels:
      app: elasticsearch
  template:
    metadata:
      labels:
        app: elasticsearch
    spec:
      containers:
        - name: elasticsearch
          image: docker.elastic.co/elasticsearch/elasticsearch-oss:7.10.2
          ports:
            - containerPort: 9200
            - containerPort: 9300
          env:
            - name: ELASTIC_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: elasticsearch-credentials
                  key: ELASTIC_PASSWORD
            - name: discovery.type
              value: "single-node"
---
apiVersion: v1
kind: Service
metadata:
  name: elasticsearch
spec:
  ports:
    - name: http
      port: 9200
      targetPort: 9200
    - name: transport
      port: 9300
      targetPort: 9300
  selector:
    app: elasticsearch
```

[kibana.yml](/kube/kibana.yml)

```yaml
---
apiVersion: v1
kind: Secret
metadata:
  name: kibana-credentials
  labels:
    app: kibana
type: Opaque
data:
  # kibana_system
  ELASTICSEARCH_USERNAME: a2liYW5hX3N5c3RlbQ==
  # password
  ELASTICSEARCH_PASSWORD: cGFzc3dvcmQ=
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: kibana-config
  labels:
    app: kibana
data:
  kibana.yml: |
    server.host: "0.0.0.0"
    elasticsearch.hosts: [ "http://elasticsearch:9200" ]
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kibana
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kibana
  template:
    metadata:
      labels:
        app: kibana
    spec:
      containers:
        - name: kibana
          image: docker.elastic.co/kibana/kibana-oss:7.10.2
          ports:
            - containerPort: 5601
          env:
            - name: ELASTICSEARCH_URL
              value: "http://elasticsearch:9200"
            - name: ELASTICSEARCH_HOSTS
              value: "http://elasticsearch:9200"
            - name: ELASTICSEARCH_USERNAME
              valueFrom:
                secretKeyRef:
                  name: kibana-credentials
                  key: ELASTICSEARCH_USERNAME
            - name: ELASTICSEARCH_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: kibana-credentials
                  key: ELASTICSEARCH_PASSWORD
          volumeMounts:
            - name: kibana-config
              mountPath: /usr/share/kibana/config/kibana.yml
              subPath: kibana.yml
      volumes:
        - name: kibana-config
          configMap:
            name: kibana-config
---
apiVersion: v1
kind: Service
metadata:
  name: kibana
spec:
  ports:
    - port: 5601
      targetPort: 5601
  selector:
    app: kibana
```

[logstash.yml](/kube/logstash.yml)

```yaml
---
apiVersion: v1
kind: Secret
metadata:
  name: logstash-credentials
  labels:
    app: logstash
type: Opaque
data:
  # logstash_system
  ELASTICSEARCH_USERNAME: bG9nc3Rhc2hfc3lzdGVt
  # password
  ELASTICSEARCH_PASSWORD: cGFzc3dvcmQ=
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: logstash-config
  labels:
    app: logstash
data:
  logstash.conf: |
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

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: logstash
spec:
  replicas: 1
  selector:
    matchLabels:
      app: logstash
  template:
    metadata:
      labels:
        app: logstash
    spec:
      containers:
        - name: logstash
          image: docker.elastic.co/logstash/logstash:7.10.2
          ports:
            - containerPort: 5044
            - containerPort: 9600
          env:
            - name: ELASTICSEARCH_URL
              value: "http://elasticsearch:9200"
            - name: ELASTICSEARCH_HOSTS
              value: "http://elasticsearch:9200"
            - name: ELASTICSEARCH_USERNAME
              valueFrom:
                secretKeyRef:
                  name: logstash-credentials
                  key: ELASTICSEARCH_USERNAME
            - name: ELASTICSEARCH_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: logstash-credentials
                  key: ELASTICSEARCH_PASSWORD
          volumeMounts:
            - name: logstash-pipeline
              mountPath: /usr/share/logstash/pipeline/logstash.conf
              subPath: logstash.conf
          readinessProbe:
            tcpSocket:
              port: 5044
            initialDelaySeconds: 10
            periodSeconds: 30
            timeoutSeconds: 10
      volumes:
        - name: logstash-pipeline
          configMap:
            name: logstash-config
---
apiVersion: v1
kind: Service
metadata:
  name: logstash
spec:
  ports:
    - name: defaultbeat
      port: 5044
      targetPort: 5044
    - name: webapi
      port: 9600
      targetPort: 9600
  selector:
    app: logstash
```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker build -t samanalishiri/application:latest .
```

```shell
kubectl apply -f ./kube/logstash.yml
```

```shell
kubectl apply -f ./kube/elasticsearch.yml
```

```shell
kubectl apply -f ./kube/kibana.yml
```

```shell
kubectl apply -f ./kube/application.yml
```

### Check Status

```shell
kubectl get all
```

### E2eTest

```shell
kubectl port-forward service/application 8080:8080
```

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Port-Forwarding

```shell
kubectl port-forward service/elasticsearch 9200:9200
```

```shell
kubectl port-forward service/kibana 5601:5601
```

```shell
kubectl port-forward service/application 8080:8080
```

### Down

```shell
kubectl delete all --all
```

```shell
kubectl delete secrets elasticsearch-credentials
```

```shell
kubectl delete secrets kibana-credentials
```

```shell
kubectl delete secrets logstash-credentials
```

```shell
kubectl delete configMap kibana-config
```

```shell
kubectl delete configMap logstash-config
```

```shell
docker image rm samanalishiri/application:latest
```

## UI

* Elasticsearch: [http://localhost:9200](http://localhost:9200)
* Kibana: [http://localhost:5601](http://localhost:5601)
* Application: [http://localhost:8080](http://localhost:8080)

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

##

**<p align="center"> [Top](#integration-of-spring-boot-with-elk-stack) </p>**