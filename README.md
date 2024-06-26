# <p align="center">Spring Boot Tutorial</p>

Spring Boot is an agile framework to use modules of Spring framework in fast and easy configuration way. For
more information refer to [Spring Boot documentation](https://spring.io/projects/spring-boot)

This tutorial provides many best practises and examples include documentation.

**In order to create a preconfigured project based on Spring Boot use the following link.**

* [Spring Boot Generator](https://start.spring.io)

## Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

## Steps

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

## Build and Test

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

##

**<p align="center"> [Top](#spring-boot-tutorial) </p>**