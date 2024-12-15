# <p align="center">Integration of Spring Boot And Apache Active MQ (Artemis)</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Apache Active MQ (Artemis).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Apache Active MQ Artemis](#apache-active-mq-artemis)
* [Apache Active MQ Artemis Use Cases](#apache-active-mq-artemis-use-cases)
* [Install Active MQ Artemis on Docker](#install-active-mq-artemis-on-docker)
* [Install Active MQ Artemis on Kubernetes](#install-active-mq-artemis-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Apache Active MQ (Artemis)](https://activemq.apache.org/components/artemis/)
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

## Apache Active MQ Artemis

<p align="justify">

There are two distribution of Apache Active MQ, [Classic](https://activemq.apache.org/components/classic/) and the other
one is [Artemis](https://activemq.apache.org/components/artemis/). This tutorial used Artemis distribution. For more
information
see [documentation](https://activemq.apache.org/components/artemis/documentation/latest/spring-integration.html)

For more information about Apache Active MQ (Artemis)  see
the [https://activemq.apache.org/components/artemis](https://activemq.apache.org/components/artemis).

</p>

## Apache Active MQ Artemis Use Cases

* Enterprise Application Integration (EAI)
* Microservices Architecture
* Event-Driven Architecture
* Monitoring and Alerts
* Data Streaming and Processing

## Install Active MQ Artemis on Docker

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

### Apply Docker Compose

Execute the following command to install Apache Active MQ Artemis.

```shell
docker compose --file docker-compose.yml --project-name artemis up --build -d

```

### Web Console

In order to access Artemis web console open [http://localhost:8161](http://localhost:8161/) in the browser.

## Install Active MQ Artemis on Kubernetes

Create the following files for installing Apache Active MQ Artemis.

### Kube Files

[artemis-deployment.yml](/kube/artemis-deployment.yml)

```yaml
# artemis-deployment.yml
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
```

[artemis-service.yml](/kube/artemis-service.yml)

```yaml
# artemis-service.yml
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

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/artemis-deployment.yml
kubectl apply -f ./kube/artemis-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Artemis from localhost through the web browser use the following command and dashboard of
Artemis is available on [http://localhost:8161](http://localhost:8161) URL.

</p>

```shell
kubectl port-forward service/artemis 8161:8161
```

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
	docker compose --file docker-compose.yml --project-name artemis up --build -d

docker-remove-container:
	docker rm artemis --force

docker-remove-image:
	docker image rm apache/activemq-artemis:latest

kube-deploy:
	kubectl apply -f ./kube/artemis-deployment.yml
	kubectl apply -f ./kube/artemis-service.yml

kube-remove:
	kubectl delete all --all

kube-port-forward-artemis:
	kubectl port-forward service/artemis 8161:8161
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-active-mq-artemis) </p>**