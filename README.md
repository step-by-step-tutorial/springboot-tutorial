# <p align="cenetr">Spring Boot Tutorial</p>

[Spring Boot Documentation](https://spring.io/projects/spring-boot)

[Spring Boot Generator](https://start.spring.io/)

## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html) 
* [Docker](https://www.docker.com/)

## Steps

* [step: hello-world](hello-world)
* [step: profile](profile)
* [step: properties](properties)
* [step: logger-log4j](properties)
* [step: event-processor](event-processor)
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
* [step: messaging-kafka](messaging-kafka)
* step: messaging-redis
* step: rest
* step: rest-exception-handling
* step: rest-hateoas
* step: websocket
* step: webflux
* step: ELK
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
mvn -f event-processor/pom.xml clean package
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
```

**<p align="center"> [Top](#spring-boot-tutorial) </p>**