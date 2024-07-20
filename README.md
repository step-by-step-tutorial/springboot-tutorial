# <p align="center">Spring Boot Tutorial</p>

Spring Boot is an agile framework to use modules of Spring framework in fast and easy configuration way. For
more information refer to [Spring Boot documentation](https://spring.io/projects/spring-boot)

This tutorial provides many best practises and examples include documentation.

**In order to create a preconfigured project based on Spring Boot use the following link.**

* [Spring Boot Generator](https://start.spring.io)

## <p align="center"> Steps </p>

* [step: hello-world](hello-world)
* [step: profile](profile)
* [step: properties](properties)
* [step: logger-log4j-console](logger-log4j2-console)
* [step: logger-log4j-file](logger-log4j2-file)
* [step: logger-log4j-database](logger-log4j2-database)
* [step: event-handling](event-handling)
* [step: rdbms-h2](rdbms-h2)
* [step: rdbms-mysql](rdbms-mysql)
* [step: rdbms-postgresql](rdbms-postgresql)
* [step: rdbms-oracle](rdbms-oracle)
* [step: nosql-redis](nosql-redis)
* [step: nosql-redis-reactive](nosql-redis-reactive)
* [step: nosql-mongodb](nosql-mongodb)
* [step: nosql-mongodb-reactive](nosql-mongodb-reactive)
* step: nosql-hadoop
* [step: messaging-apache-active-mq (artemis)](messaging-apache-artemis-mq)
* [step: messaging-rabbit-mq](messaging-rabbit-mq)
* [step: messaging-apache-kafka](messaging-apache-kafka)
* step: messaging-redis
* step: messaging-pulsar
* [step: streaming-apache-kafka](streaming-apache-kafka)
* [step: restful-web-api](restful-web-api)
* step: rest-hateoas
* step: webflux
* step: ELK
* step: Jaeger
* step: security-authentication-basic
* step: security-authentication-inmemory
* step: security-authentication-UserEntity-service
* step: security-authentication-ldap
* step: security-authentication-remember-me
* step: security-authentication-csrf
* step: security-authentication-cors
* step: security-authentication-xss
* step: security-authentication-mfa
* step: security-authentication-open-id
* step: security-authorization-rbac
* step: security-authorization-abac
* step: security-authorization-acl
* step: oauth-authorization-code
* step: oauth-password
* step: oauth-client-credentials
* step: oauth-Implicit

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

### Pipeline

#### Clean Repository

[clean-all.sh](clean-all.sh)

```shell
mvn -f hello-world/pom.xml clean
mvn -f profile/pom.xml clean
mvn -f properties/pom.xml clean
mvn -f logger-log4j2-console/pom.xml clean
mvn -f logger-log4j2-file/pom.xml clean
mvn -f logger-log4j2-database/pom.xml clean
mvn -f event-handling/pom.xml clean
mvn -f rdbms-h2/pom.xml clean
mvn -f rdbms-mysql/pom.xml clean
mvn -f rdbms-postgresql/pom.xml clean
mvn -f rdbms-oracle/pom.xml clean
mvn -f nosql-redis/pom.xml clean
mvn -f nosql-redis-reactive/pom.xml clean
mvn -f nosql-mongodb/pom.xml clean
mvn -f nosql-mongodb-reactive/pom.xml clean
mvn -f messaging-apache-artemis-mq/pom.xml clean
mvn -f messaging-rabbit-mq/pom.xml clean
mvn -f messaging-apache-kafka/pom.xml clean
mvn -f streaming-apache-kafka/pom.xml clean
```

#### Clean Docker

[docker-clean.sh](docker-clean.sh)

```shell
docker container prune
docker image prune
docker volume prune
docker network prune
docker system prune
docker system prune -a
docker system prune -a --volumes
```

[build-all.sh](build-all.sh)

#### Build and Test

```shell
mvn -f hello-world/pom.xml clean package
mvn -f profile/pom.xml clean package
mvn -f properties/pom.xml clean package
mvn -f logger-log4j2-console/pom.xml clean package
mvn -f logger-log4j2-file/pom.xml clean package
mvn -f logger-log4j2-database/pom.xml clean package
mvn -f event-handling/pom.xml clean package
mvn -f rdbms-h2/pom.xml clean package
mvn -f rdbms-mysql/pom.xml clean package
mvn -f rdbms-postgresql/pom.xml clean package
mvn -f rdbms-oracle/pom.xml clean package
mvn -f nosql-redis/pom.xml clean package
mvn -f nosql-redis-reactive/pom.xml clean package
mvn -f nosql-mongodb/pom.xml clean package
mvn -f nosql-mongodb-reactive/pom.xml clean package
mvn -f messaging-apache-artemis-mq/pom.xml clean package
mvn -f messaging-rabbit-mq/pom.xml clean package
mvn -f messaging-apache-kafka/pom.xml clean package
mvn -f restful-web-api/pom.xml clean package
```

## Install Tools on Docker

### Docker Compose

[docker-compose.yml](docker-compose.yml)

### Apply Docker Compose

Execute the following command to install tools.

```shell
docker compose --file .\docker-compose.yml --project-name springboot_tutorial up -d

```

### Remove Docker Compose

Execute the following command to remove containers and images.

#### Remove Docker Containers

[docker-rm-containers](docker-rm-containers.sh)

```shell
# remove containers
docker rm -f \
  mysql \
  mysql-workbench \
  postgresql \
  pgadmin \
  oracle \
  ords \
  adminer \
  phpmyadmin \
  redis \
  redisinsight \
  commander \
  mongo \
  mongo-express \
  rabbitmq \
  artemis \
  zookeeper \
  kafka \
  kafdrop
```

#### Remove Docker Images

[docker-rm-images](docker-rm-images.sh)

```shell
# remove images
docker rmi \
  mysql:8.0 \
  lscr.io/linuxserver/mysql-workbench:latest \
  postgres:13.9-alpine \
  dpage/pgadmin4 \
  container-registry.oracle.com/database/express:21.3.0-xe \
  container-registry.oracle.com/database/ords:latest \
  adminer \
  phpmyadmin \
  redis:latest \
  redislabs/redisinsight:latest \
  rediscommander/redis-commander:latest \
  mongo \
  mongo-express \
  rabbitmq:management \
  apache/activemq-artemis:latest \
  docker.io/bitnami/zookeeper \
  docker.io/bitnami/kafka \
  obsidiandynamics/kafdrop:latest
```

#### Remove Docker Volumes

```shell
docker volume rm ords_config oracle_config
```

#### Remove Docker Network

```shell
docker network rm springboot_tutorial_default
```

##

**<p align="center"> [Top](#spring-boot-tutorial) </p>**