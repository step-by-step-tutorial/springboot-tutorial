# <p align="center">Integration of Spring Boot And Apache Active MQ (Artemis)</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Apache Active MQ (Artemis).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Apache Active MQ Artemis](#apache-active-mq-artemis)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui )
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)

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
```

## Apache Active MQ Artemis

<p align="justify">

There are two distribution of Apache Active MQ, [Classic](https://activemq.apache.org/components/classic/) and the other
one is [Artemis](https://activemq.apache.org/components/artemis/). This tutorial used Artemis distribution. For more
information
see [documentation](https://activemq.apache.org/components/artemis/documentation/latest/spring-integration.html)

For more information about Apache Active MQ (Artemis)  see
the [https://activemq.apache.org/components/artemis](https://activemq.apache.org/components/artemis).

</p>

### Apache Active MQ Artemis Use Cases

* Enterprise Application Integration (EAI)
* Microservices Architecture
* Event-Driven Architecture
* Monitoring and Alerts
* Data Streaming and Processing

## Dockerized

### Docker Compose

Create a file named `docker-compose.yml` with the following configuration.

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: '3.8'

services:
  artemis:
    image: apache/activemq-artemis:latest
    container_name: artemis
    hostname: artemis
    restart: always
    ports:
      - "6161:61616"
      - "8161:8161"
    volumes:
      - "./target/broker:/opt/broker"
      - "./target/broker:/opt/artemis"
```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker compose --file docker-compose.yml --project-name dev-env up --build -d
```

### Web Console

In order to access Artemis web console open [http://localhost:8161](http://localhost:8161/) in the browser.

### Down

```shell
docker compose --file docker-compose.yml --project-name dev-env down
```

## Kubernetes

Create the following files for installing Apache Active MQ Artemis.

### Kube Files

[artemis-deployment.yml](/kube/artemis-deployment.yml)

```yaml
# artemis.yml
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: artemis
  labels:
    app: artemis
spec:
  replicas: 1
  selector:
    matchLabels:
      app: artemis
  template:
    metadata:
      labels:
        app: artemis
    spec:
      containers:
        - name: artemis
          image: apache/activemq-artemis:latest
          ports:
            - containerPort: 6161
            - containerPort: 8161
---
apiVersion: v1
kind: Service
metadata:
  name: artemis
spec:
  selector:
    app: artemis
  ports:
    - name: queue-port
      port: 6161
      targetPort: 6161
    - name: management-ui-port
      port: 8161
      targetPort: 8161

```

### Deploy

```shell
mvn clean package verify -DskipTests=true
```

```shell
docker build -t samanalishiri/application:latest .
```

```shell
kubectl apply -f ./kube/artemis.yml
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
```

### Port Forwarding

```shell
kubectl port-forward service/artemis 8161:8161
```

```shell
kubectl port-forward service/artemis 8080:8080
```

### Down

```shell
kubectl delete all --all
```

```shell
docker image rm samanalishiri/application:latest
```

## UI

* Artemis: [http://localhost:8161](http://localhost:8161)
* Application: [http://localhost:8080](http://localhost:8080)

## How To Set up Spring Boot

### Dependencies

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-artemis</artifactId>
</dependency>
```

### Application Properties

```yaml
spring:
  artemis:
    mode: native
    user: ${ACTIVE_MQ_USER:artemis}
    password: ${ACTIVE_MQ_PASS:artemis}
    broker-url: ${ACTIVE_MQ_HOST:localhost}:${ACTIVE_MQ_PORT:6161}
  jms:
    pub-sub-domain: true


```

### Java Config

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class JmsConfig {
}
```

## How To Set up Spring Boot Test

The embedded Active-MQ used for unit tests.

### Dependencies

```xml

<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>artemis-jms-server</artifactId>
    <version>2.29.0</version>
</dependency>
```

### Application Properties

```yaml
spring:
  config:
    activate:
      on-profile: embedded-artemis
  artemis:
    mode: embedded
    embedded:
      enabled: true
    broker-url: localhost:6161
```

### Java Config

```java

import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"embedded-artemis"})
public class EmbeddedActiveMqServer implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedActiveMqServer.class);

    private final EmbeddedActiveMQ activeMqServer = new EmbeddedActiveMQ();

    public EmbeddedActiveMqServer(
            @Value("${spring.artemis.host}") final String host,
            @Value("${spring.artemis.port}") final String port
    ) {
        try {
            org.apache.activemq.artemis.core.config.Configuration config = new ConfigurationImpl();
            config.addAcceptorConfiguration("embeddedartemis", String.format("tcp://%s:%s", host, port));
            activeMqServer.setConfiguration(config);
            activeMqServer.start();
            logger.info("Embedded active-mq-artemis has started");
        } catch (Exception e) {
            logger.error("Embedded active-mq-artemis failed due to: {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("Embedded active-mq-artemis has stopped");
        activeMqServer.stop();
    }
}
```

Use `embedded-artemis` for active profile.

```java

@SpringBootTest
@ActiveProfiles({"embedded-artemis"})
class TestClass {

}

```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-active-mq-artemis) </p>**