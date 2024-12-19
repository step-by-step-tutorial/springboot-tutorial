# <p align="center">Integration of Spring Boot And Jaeger</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Jaeger.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Jaeger](#jaeger)
* [Jaeger Use Cases](#jaeger-use-cases)
* [Install Jaeger on Docker](#install-jaeger-on-docker)
* [Install Jaeger on Kubernetes](#install-jaeger-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Jaeger](https://www.jaegertracing.io)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

### Pipeline

#### Build

```shell
mvn clean package -DskipTests=true 
```

#### Test

```shell
mvn test
```

#### Run

```shell
mvn  spring-boot:run
```

```shell
curl http://localhost:8080/api/v1/application/status
```

## Jaeger

<p align="justify">

It is designed to monitor and troubleshoot microservices-based applications. Jaeger provides end-to-end visibility into
how requests traverse through a system, helping developers and operators understand system performance and identify
bottlenecks.

</p>

<p align="justify">

Jaeger is often used in environments where applications are deployed as distributed systems, such as microservices
architectures, serverless computing, and containerized environments.

</p>

<p align="justify">

For more information about Jaeger see the [https://www.jaegertracing.io](https://www.jaegertracing.io).

</p>

## Jaeger Use Cases

* Performance Monitoring and Optimization
* Troubleshooting, Debugging, and Root Cause Analysis
* Service Dependency Visualization

## Install Jaeger on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: '3.9'
services:
  jaeger:
    container_name: jaeger
    hostname: jaeger
    image: jaegertracing/all-in-one:latest
    ports:
      - "16686:16686"
      - "4318:4318"
  observabilityjaeger:
    image: samanalishiri/observabilityjaeger:latest
    build:
      context: .
      dockerfile: ./Dockerfile
    container_name: observabilityjaeger
    hostname: observabilityjaeger
    restart: always
    ports:
      - "8080:8080"
    environment:
      APP_HOST: "0.0.0.0"
      APP_PORT: "8080"
      TRACING_HOST: http://jaeger:4318/v1/traces
```

### Apply Docker Compose

Execute the following command to install Jaeger.

```shell
docker compose --file ./docker-compose.yml --project-name observabilityjaeger up --build -d
```

### Web Console

<p align="justify">

In order to connect to Jaeger open [http://localhost:16686](http://localhost:16686) through web browser. After first
interaction with the application you can see the traces, events, etc., under your registered application as a service.

</p>

## Install Jaeger on Kubernetes

Create the following files for installing Jaeger.

### Kube Files

[jaeger-deployment.yml](/kube/jaeger-deployment.yml)

```yaml
#deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: jaeger
spec:
  replicas: 1
  selector:
    matchLabels:
      app: jaeger
  template:
    metadata:
      labels:
        app: jaeger
    spec:
      containers:
        - name: jaeger
          image: jaegertracing/all-in-one:latest
          ports:
            - containerPort: 16686
            - containerPort: 4318
```

[jaeger-service.yml](/kube/jaeger-service.yml)

```yaml
#service.yml
apiVersion: v1
kind: Service
metadata:
  name: jaeger
spec:
  selector:
    app: jaeger
  ports:
    - name: console
      port: 16686
      targetPort: 16686
    - name: api
      port: 4318
      targetPort: 4318
```

[app-deployment.yml](/kube/app-deployment.yml)

```yaml
#deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: observabilityjaeger
spec:
  replicas: 1
  selector:
    matchLabels:
      app: observabilityjaeger
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: observabilityjaeger
    spec:
      containers:
        - name: observabilityjaeger
          image: samanalishiri/observabilityjaeger:latest
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
          env:
            - name: APP_HOST
              value: "0.0.0.0"
            - name: APP_PORT
              value: "8080"
            - name: TRACING_HOST
              value: "http://jaeger:4318/v1/traces"
```

[app-service.yml](/kube/app-service.yml)

```yaml
#service.yml
apiVersion: v1
kind: Service
metadata:
  name: observabilityjaeger
spec:
  selector:
    app: observabilityjaeger
  ports:
    - port: 8080
      targetPort: 8080
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/jaeger-deployment.yml
kubectl apply -f ./kube/jaeger-service.yml
kubectl apply -f ./kube/app-deployment.yml
kubectl apply -f ./kube/app-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Jaeger from localhost through the web browser use the following command and dashboard of
Jaeger is available on [http://localhost:16686](http://localhost:16686) URL.

</p>

```shell
kubectl port-forward service/jaeger 16686:16686
```

```shell
kubectl port-forward service/jaeger 4318:4318
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
    <!--spring-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--opentelemetry-->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-tracing-bridge-otel</artifactId>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-exporter-otlp</artifactId>
    </dependency>
</dependencies>
```

### Application Properties

```yaml
server:
  address: ${APP_HOST:0.0.0.0}
  port: ${APP_PORT:8080}
spring:
  application:
    name: observability-jaeger
management:
  tracing:
    enabled: true
    sampling:
      probability: 1.0
  otlp:
    tracing:
      endpoint: ${TRACING_HOST:http://localhost:4318/v1/traces}

```

## How To Set up Spring Boot Test

### Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Application Properties

```yaml
spring:
  config:
    activate:
      on-profile: test
management:
  otlp:
    tracing:
      endpoint: http://localhost:4318/v1/traces
```

### Java Config

```java
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationApiTests {

    static final String HOST = "http://localhost";

    static final String ROOT_URI = "/api/v1/application";

    static final Logger LOGGER = LoggerFactory.getLogger(ApplicationApiTests.class.getSimpleName());

    @Container
    static final GenericContainer<?> JAEGER = new GenericContainer<>("jaegertracing/all-in-one:latest");

    static {
        try {
            JAEGER.setExposedPorts(List.of(16686, 4318));
        } catch (Exception e) {
            LOGGER.error("Start jaeger container failed due to: {}", e.getMessage());
        }
    }

    @BeforeAll
    static void beforeAll() {
        JAEGER.start();
    }

    @AfterAll
    static void afterAll() {
        JAEGER.stop();
    }

    @LocalServerPort
    int port;
}
```

## Appendix

### Makefile

```makefile
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/observabilityjaeger:latest .

docker-deploy:
	docker run \
	--name observabilityjaeger \
	-p 8080:8080 \
	-h observabilityjaeger \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-e TRACING_HOST=http://jaeger:4318/v1/traces \
	-itd samanalishiri/observabilityjaeger:latest

docker-compose-deploy:
	docker compose --file ./docker-compose.yml --project-name observabilityjaeger up --build -d

docker-remove-container:
	docker rm observabilityjaeger --force

docker-remove-image:
	docker image rm samanalishiri/observabilityjaeger:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/observabilityjaeger 8080:8080

kube-port-forward-jaeger-ui:
	kubectl port-forward service/jaeger 16686:16686

kube-port-forward-jaeger:
	kubectl port-forward service/jaeger 4318:4318
```

##

**<p align="center"> [Top](#title) </p>**