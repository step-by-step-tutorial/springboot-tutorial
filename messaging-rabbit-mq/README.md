# <p align="center">Rabbit MQ</p>

<p align="justify">

RabbitMq is a message queue for more information see the [https://www.rabbitmq.com/](https://www.rabbitmq.com/).

</p>

## Install RabbitMQ on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

```yaml
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

Execute the `docker compose  up -d` command to install RabbitMQ.

```shell
# full command
docker compose --file ./docker-compose.yml --project-name rabbitmq up --build -d

```

### Web Console

Open [http://localhost:15672/](http://localhost:15672/) in the browser.

## Install RabbitMQ on Kubernetes

Create the following files for installing RabbitMQ.

**rabbitmq-secrets.yml**

```yaml
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

**rabbitmq-deployment.yml**

```yaml
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

**rabbitmq-service.yml**

```yaml
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

### Apply Configuration Files

Execute the following commands to install the tools on Kubernetes.

```shell
# ======================================================================================================================
# Rabbitmq
# ======================================================================================================================
kubectl apply -f ./kube/rabbitmq-secrets.yml
# kubectl describe secret rabbitmq-secrets -n default
# kubectl get secret rabbitmq-secrets -n default -o yaml

kubectl apply -f ./kube/rabbitmq-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment rabbitmq -n default

kubectl apply -f ./kube/rabbitmq-service.yml
# kubectl get service -n default
# kubectl describe service rabbitmq -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all
```

In order to connect to rabbitmq from localhost through the web browser or application use the following command.

```shell
# rabbitmq
# http://localhost:15672
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

## How To Set up Test

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

### Java Config for Test

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

## Prerequisites

* [Java 21](https://www.oracle.com/de/java/technologies/downloads/)
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

##

**<p align="center"> [Top](#rabbit-mq) </p>**