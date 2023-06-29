# <p aligin="cenetr">Spring Boot Tutorial</p>

[Spring Boot Documentation](https://spring.io/projects/spring-boot)

[Spring Boot Generator](https://start.spring.io/)

## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html) 
* [Docker](https://www.docker.com/)

## Steps

* [step 001: hello-world](hello-world)
* [step 002: profile](profile)
* [step 003: properties](properties)
* [step 004: listener](listener)
* [step 005: rdbms-h2](rdbms-h2)
* [step 006: rdbms-mysql](rdbms-mysql)
* [step 007: rdbms-postgresql](rdbms-postgresql)
* [step 008: rdbms-oracle](rdbms-oracle)
* [step 009: nosql-redis](nosql-redis)
* [step 010: nosql-redis-reactive](nosql-redis-reactive)
* [step 011: nosql-mongodb](nosql-mongodb)
* [step 012: nosql-mongodb-reactive](nosql-mongodb-reactive)
* [step 013: messaging-apache-active-mq (artemis)](messaging-apache-artemis-mq)
* step 014: messaging-rabbit-mq
* step 015: messaging-kafka
* step 016: rest
* step 017: rest-exception-handling
* step 018: rest-hateoas
* step 019: websocket
* step 020: webflux
* step 021: ELK
* step 022: security-authentication-basic
* step 023: security-authentication-inmemory
* step 024: security-authentication-UserEntity-service
* step 025: security-authentication-ldap
* step 026: security-authentication-remember-me
* step 027: security-authentication-csrf
* step 028: security-authentication-cors
* step 029: security-authentication-xss
* step 030: security-authentication-mfa
* step 031: security-authentication-open-id
* step 032: security-authorization-rbac
* step 033: security-authorization-abac
* step 034: security-authorization-acl
* step 035: oauth-authorization-code
* step 036: oauth-password
* step 037: oauth-client-credentials
* step 038: oauth-Implicit

## Build and Test

```shell
mvn -f hello-world/pom.xml clean package
mvn -f profile/pom.xml clean package
mvn -f properties/pom.xml clean package
mvn -f listener/pom.xml clean package
mvn -f rdbms-h2/pom.xml clean package
mvn -f rdbms-mysql/pom.xml clean package
mvn -f rdbms-postgresql/pom.xml clean package
mvn -f nosql-redis/pom.xml clean package
mvn -f nosql-redis-reactivr/pom.xml clean package
mvn -f nosql-mongodb/pom.xml clean package
mvn -f nosql-mongodb-reactive/pom.xml clean package
```

**<p align="center"> [Top](#spring-boot-tutorial) </p>**