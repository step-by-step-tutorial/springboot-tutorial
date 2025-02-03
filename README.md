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
* [step: messaging-apache-active-mq (artemis)](messaging-apache-artemis-mq)
* [step: messaging-rabbit-mq](messaging-rabbit-mq)
* [step: messaging-apache-kafka](messaging-apache-kafka)
* [step: messaging-redis](messaging-redis)
* [step: messaging-pulsar](messaging-pulsar)
* [step: streaming-apache-kafka](streaming-apache-kafka)
* [step: restful-web-api](restful-web-api)
* step: rest-hateoas
* step: webflux
* [step: cdc-debezium](cdc-debezium)
* [step: cdc-embedded-debezium](cdc-embedded-debezium)
* [step: log-aggregation-elk](log-aggregation-elk)
* [step: observability-grafana-stack](observability-grafana-stack)
* [step: observability-Jaeger](observability-jaeger)
* [step: security-rbac-inmemory](security-rbac-inmemory)
* [step: security-dynamic-policy](security-dynamic-policy)
* [step: security-rbac-jwt](security-rbac-jwt)
    * RBAC
    * JWT
    * CORS
* step: security-rbac-web
    * remember-me
    * CSRF
    * XSS
* step: security-authentication-ldap
* step: security-authentication-mfa
* step: security-authentication-open-id
* step: security-authorization-acl
* [step: security-oauth](security-oauth2)
    * authorization-code
    * oauth-password
    * oauth-client-credentials
    * oauth-Implicit

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

### Clean Repository

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
mvn -f messaging-redis/pom.xml clean
mvn -f messaging-pulsar/pom.xml clean
mvn -f streaming-apache-kafka/pom.xml clean
mvn -f streaming-redis/pom.xml clean
mvn -f cdc-debezium/pom.xml clean
mvn -f cdc-embedded-debezium/pom.xml clean
mvn -f log-aggregation-elk/pom.xml clean
mvn -f observability-grafana-stack/pom.xml clean
mvn -f observability-jaeger/pom.xml clean
mvn -f security-rbac-inmemory/pom.xml clean
mvn -f security-rbac-jwt/pom.xml clean
```

### Clean Docker

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

### Build and Test

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
mvn -f messaging-pulsar/pom.xml clean package
mvn -f messaging-redis/pom.xml clean package
mvn -f streaming-apache-kafka/pom.xml clean package
mvn -f cdc-debezium/pom.xml clean package
mvn -f cdc-embedded-debezium/pom.xml clean package
mvn -f log-aggregation-elk/pom.xml clean package
mvn -f observability-grafana-stack/pom.xml clean package
mvn -f observability-jaeger/pom.xml clean package
mvn -f restful-web-api/pom.xml clean package
mvn -f security-rbac-inmemory/pom.xml clean package
mvn -f security-rbac-jwt/pom.xml clean package
```

##

**<p align="center"> [Top](#spring-boot-tutorial) </p>**