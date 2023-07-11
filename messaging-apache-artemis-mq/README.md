# <p align="center">Apache Active MQ (Artemis)</p>

<p>

There are two distribution of Apache Active MQ, [Classic](https://activemq.apache.org/components/classic/) and the other
one is [Artemis](https://activemq.apache.org/components/artemis/). This tutorial used Artemis distribution.

</p>

## Install Active MQ Artemis on Docker

Create docker-compose.yml, Dockerfile and start.sh file in a directory then execute the `docker compose  up -d` command
to install Artemis on docker, also, you can use the following commands.

**Help**
```shell
# check if docker was install on your machine
docker --version
docker-compose --version
docker-machine --version

# remove current container and image
docker rm artemis --force
docker image rm apache/activemq-artemis:latest

# install and deploy artemis
docker compose --file docker-compose.yml --project-name artemis up --build -d
```

**Docker Compose file**
```yaml
version: '3.8'

services:
  artemis:
    image: apache/activemq-artemis
    build:
      context: ./
      dockerfile: Dockerfile
    container_name: artemis
    hostname: artemis
    restart: always
    ports:
      - "61616:61616"
      - "8161:8161"
#    volumes:
##     - ~/your local host path: exported volumes in docker file
#      - ~/broker:/opt/broker
#      - ~/broker:/opt/artemis
```
**Docker File**
```dockerfile
FROM openjdk:11
MAINTAINER samanalishiri@gmail.com

# Build arguments
ARG USER=artemis
ARG GROUP=ops
ARG USER_HOME=/home/$USER
ARG WORK_DIRECTORY=/opt
ARG APP_HOME=$WORK_DIRECTORY/artemis
ARG APP_VERSION=2.29.0
ARG BROKER_HOME=$WORK_DIRECTORY/broker

# environment variable
ENV USER=$USER
ENV GROUP=$GROUP
ENV HOME=$USER_HOME
ENV WORK_DIRECTORY=$WORK_DIRECTORY
ENV APP_HOME=$APP_HOME
ENV APP_VERSION=$APP_VERSION
ENV BROKER_HOME=$BROKER_HOME
ENV APP_USER=artemis
ENV APP_PASSWORD=artemis
# there are two options for ANONYMOUS_LOGIN
# 1: --require-login
# 2: --allow-anonymous
ENV ANONYMOUS_LOGIN="--allow-anonymous"
ENV HTTP_HOST="0.0.0.0"
ENV EXTRA_ARGS="--relax-jolokia"

RUN groupadd $GROUP
RUN useradd -d /home/$USER -g $GROUP -m -s /bin/bash $USER

RUN mkdir -p  $APP_HOME
RUN chown -R $USER:$GROUP $APP_HOME

RUN mkdir $BROKER_HOME
RUN chown -R $USER:$GROUP $BROKER_HOME

# update OS
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y libaio1
RUN rm -rf /var/lib/apt/lists/*
# download and install apache active mq artemis
RUN curl -L https://downloads.apache.org/activemq/activemq-artemis/$APP_VERSION/apache-artemis-$APP_VERSION-bin.tar.gz -o artemis.tar.gz
RUN tar -xzf artemis.tar.gz --strip-components=1 -C $APP_HOME
RUN rm artemis.tar.gz

COPY ./start.sh $WORK_DIRECTORY
RUN chmod 755 -R $WORK_DIRECTORY/start.sh

# Web Server
EXPOSE 8161 \
# JMX Exporter
    9404 \
# Port for CORE,MQTT,AMQP,HORNETQ,STOMP,OPENWIRE
    61616 \
# Port for HORNETQ,STOMP
    5445 \
# Port for AMQP
    5672 \
# Port for MQTT
    1883 \
#Port for STOMP
    61613

USER artemis

VOLUME ["$APP_HOME", "$BROKER_HOME"]
#WORKDIR $APP_HOME
CMD $WORK_DIRECTORY/start.sh
```

**Start File**

```shell
set -e

if ! [ -f "${BROKER_HOME}"/etc/broker.xml ]; then
   "$APP_HOME"/bin/artemis create --user "${APP_USER}" --password "${APP_PASSWORD}" \
               --silent \
               "${LOGIN_OPTION}" \
               --http-host "${HTTP_HOST}" \
               "${EXTRA_ARGS}" \
               "${BROKER_HOME}"
else
  echo "broker already created, ignoring creation"
fi

exec  "${BROKER_HOME}"/bin/artemis run
```

### Web Console
Open [http://localhost:8161](http://localhost:8161/) in the browser.

## How To Config Spring Boot

### Dependencies

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-artemis</artifactId>
</dependency>
```

### Spring Boot Properties

```yaml
spring:
  artemis:
    mode: native
    host: ${ACTIVE_MQ_HOST:localhost}
    port: ${ACTIVE_MQ_PORT:61616}
    user: ${ACTIVE_MQ_USER:artemis}
    password: ${ACTIVE_MQ_PASS:artemis}
  jms:
    pub-sub-domain: true

```

### How to Active JMS

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class JmsConfig {
}
```

## How To Config Embedded Active MQ

### Dependencies

```xml

<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>artemis-jms-server</artifactId>
    <version>2.29.0</version>
</dependency>
```

### Spring Boot Properties

```yaml
spring:
  artemis:
    mode: embedded
    embedded:
      enabled: true
      queues: mainQueue,statusQueue
    host: localhost
    port: 61616
```

### How to Use Embedded Server

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
public class EmbeddedActiveMqServer implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedActiveMqServer.class);

    private final EmbeddedActiveMQ activeMqServer = new EmbeddedActiveMQ();

    public EmbeddedActiveMqServer(
            @Value("${spring.artemis.host}") final String host,
            @Value("${spring.artemis.port}") final String port
    ) {
        try {
            org.apache.activemq.artemis.core.config.Configuration config = new ConfigurationImpl();
            config.addAcceptorConfiguration("tcp", String.format("tcp://%s:%s", host, port));
            activeMqServer.setConfiguration(config);
            activeMqServer.start();
            logger.info("embedded active-mq has started");
        } catch (Exception e) {
            logger.error("embedded active-mq failed due to: {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("embedded active-mq has stopped");
        activeMqServer.stop();
    }
}
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

**<p align="center"> [Top](#apache-active-mq-artemis) </p>**