# <p align="center">Integration of Spring Boot And Rabbit MQ</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Rabbit MQ.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Rabbit MQ](#rabbit-mq)
* [Rabbit MQ Use Cases](#rabbit-mq-use-cases)
* [Install Rabbit MQ on Docker](#install-rabbitmq-on-docker)
* [Install Rabbit MQ on Kubernetes](#install-rabbitmq-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Rabbitmq](https://www.rabbitmq.com)
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

## Rabbit MQ

RabbitMq is a message queue for more information see the [https://www.rabbitmq.com](https://www.rabbitmq.com).

## Rabbit MQ Use Cases

* Application Integration
* Microservices Communication
* Event-Driven Systems
* Task and Job Queue Management
* Real-Time Data Processing
* Notification and Alert Systems
* Monitoring and Logging
* Workflow Orchestration

## Install RabbitMQ on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq:management
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      - RABBITMQ_DEFAULT_USER=guest
      - RABBITMQ_DEFAULT_PASS=guest
```

### Apply Docker Compose

Execute the following command to install RabbitMQ.

```shell
docker compose --file ./docker-compose.yml --project-name rabbitmq up --build -d

```

### Web Console

Open [http://localhost:15672](http://localhost:15672) in the browser.

## Install RabbitMQ on Kubernetes

Create the following files for installing RabbitMQ.

### Kube Files

[rabbitmq-secrets.yml](/kube/rabbitmq-secrets.yml)

```yaml
#rabbitmq-secrets.yml
apiVersion: v1
kind: Secret
metadata:
  name: rabbitmq-secrets
type: Opaque
data:
  # value: root
  username: cm9vdA==
  # value: root
  password: cm9vdA==
```

[rabbitmq-deployment.yml](/kube/rabbitmq-deployment.yml)

```yaml
#rabbitmq-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: rabbitmq
  labels:
    app: rabbitmq
spec:
  replicas: 1
  selector:
    matchLabels:
      app: rabbitmq
  template:
    metadata:
      labels:
        app: rabbitmq
    spec:
      containers:
        - name: rabbitmq
          image: rabbitmq:management
          ports:
            - containerPort: 5672
            - containerPort: 15672
          env:
            - name: RABBITMQ_DEFAULT_USER
              valueFrom:
                secretKeyRef:
                  name: rabbitmq-secrets
                  key: username
            - name: RABBITMQ_DEFAULT_PASS
              valueFrom:
                secretKeyRef:
                  name: rabbitmq-secrets
                  key: password
```

[rabbitmq-service.yml](/kube/rabbitmq-service.yml)

```yaml
#rabbitmq-service.yml
apiVersion: v1
kind: Service
metadata:
  name: rabbitmq
spec:
  selector:
    app: rabbitmq
  ports:
    - name: queue-port
      port: 5672
      targetPort: 5672
    - name: management-ui-port
      port: 15672
      targetPort: 15672
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/rabbitmq-secrets.yml
kubectl apply -f ./kube/rabbitmq-deployment.yml
kubectl apply -f ./kube/rabbitmq-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to rabbitmq from localhost through the web browser use the following command and dashboard of
rabbitmq is available on [http://localhost:15672](http://localhost:15672) URL.

</p>

```shell
kubectl port-forward service/rabbitmq 15672:15672
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

```

### Application Properties

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: root
    password: root

```

### Java Config

```java

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {
    @Bean
    public Queue queue(@Value("${queue-name}") final String name) {
        return new Queue(name, false);
    }
}
```

## How To Set up Spring Boot Test

### Dependencies

```xml

<project>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.testcontainers</groupId>
                <artifactId>testcontainers-bom</artifactId>
                <version>1.18.3</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>rabbitmq</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### Java Config

```java

@Testcontainers
class TestClass {
    @Container
    static final RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:management");

    @DynamicPropertySource
    static void rabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMqContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMqContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMqContainer::getAdminPassword);
    }

    @BeforeAll
    static void beforeAll() {
        rabbitMqContainer.start();
    }

    @AfterAll
    static void afterAll() {
        rabbitMqContainer.start();
    }
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

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name rabbitmq up --build -d

docker-remove-container:
	docker rm rabbitmq --force

docker-remove-image:
	docker image rm rabbitmq:management

kube-deploy:
	kubectl apply -f ./kube/rabbitmq-secrets.yml
	kubectl apply -f ./kube/rabbitmq-deployment.yml
	kubectl apply -f ./kube/rabbitmq-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets rabbitmq-secrets

kube-port-forward-rabbitmq:
	kubectl port-forward service/rabbitmq 15672:15672
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-rabbit-mq) </p>**