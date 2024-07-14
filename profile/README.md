# <p align="center">Spring Boot Profiling</p>

<p align="justify">

This tutorial is about working with profiles in the Spring Boot. The Profiles provide a possibility to categorized
classes in order to use for specific goals, for example define specific classes only for test environment, etc.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Apply Profiling](#apply-profiling)
* [Active Profile](#active-profile)
* [Scenario](#scenario)
* [Appendix](#appendix)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)

### Pipeline

#### Build

```bash
mvn clean package -DskipTests=true 
```

#### Test

```bash
mvn test
```

#### Run

```bash
mvn  spring-boot:run
```

## Apply Profiling

<p align="justify">

In order to apply profiling:

* For classes it uses `@Profile` annotation
* In `application.yml` file there are two solutions
    * Use `---` and `spring.config.activate.on-profile=profile-name` property in yaml format
    * The `application.yml` should be included profile name with hyphen as separator, for
      example `application-test.yml`.
* In `application.properties` file there are two solutions
    * Use `spring.config.activate.on-profile=profile-name` property as separator
    * The `application.properties` should be included profile name with hyphen as separator, for
      example `application-test.yml`.

</p>

## Active Profile

There are a few solutions for that as follows.

### JVM

Execute the following command.

```shell
java -jar application.jar -Dspring.profiles.active=profile-name
```

### ENV

Add the following variable as an environment variable.

```dotenv
SPRING_PROFILES_ACTIVE=profile-name
```

### Properties

Add the following properties in `application.yml/properties`.

```properties
spring.profiles.active=profile-name
```

### Maven

Add a profile as follows in the pom.xml file.

```xml

<profile>
    <id>profile-name</id>
    <properties>
        <spring.profiles.active>profile-name</spring.profiles.active>
    </properties>
</profile>
```

Execute Maven command include the name of profile.

```shell
mvn lifecycle -P profile-name
```

### Test Environment

In order to activate a profile in test environment it uses `@ActiveProfiles("profile-name")` on top of test class.

```java

@ActiveProfiles("profile-name")
class Test {

}
```

## Scenario

In this sample there are two profiles named **dev** and **test**.

* if, do not set active profile, spring container scans all classes do not use `@Profile`
* if active profile set to **dev**, spring container scans all decorated classes with `@Profile("dev")` and classes do
  not use `@Profile`
* if active profile set to **test**, spring container scans all decorated classes with `@Profile("test")` and classes do
  not use `@Profile`

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
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
```

##

**<p align="center"> [Top](#spring-boot-profiling) </p>**