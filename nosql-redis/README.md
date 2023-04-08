# <p align="center">NOSQL Redis</p>

<p align="justify">

This tutorial is included [Redis](https://redis.io/) configuration for test and none test environment. This tutorial
will use two connection factories for making connection to the Redis.

* [Jedis](https://redis.io/docs/clients/java/)
* [Lettuce](https://lettuce.io/)

</p>



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
mvn  test
```

## Run

```bash
mvn  spring-boot:run
```

**<p align="center"> [Top](#nosql-redis) </p>**