# <p align="center">Rabbit MQ</p>

<p>


</p>

## Install Active MQ Artemis on Docker

Execute the `docker compose  up -d` command
to install RabbitMQ on docker, also, you can use the following commands.

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

```
**Docker File**
```dockerfile
```

## How To Config Spring Boot

### Dependency

```xml

```

### Spring Boot Properties

```yaml

```

### How to Active JMS

```java

```

## How To Config Embedded Rabbit MQ

### Dependency

```xml

```

### Spring Boot Properties

```yaml

```

### How to Use Embedded Server

```java

```

### Web Console

Open [http://localhost:15672/](http://localhost:15672/) in the browser.

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

**<p align="center"> [Top](#rabbit-mq) </p>**