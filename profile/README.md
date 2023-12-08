# <p align="center">Profile</p>

<p align="justify">

This sample is about working with profiles in the Spring Boot. The Profiles provide a possibility to categorized classes
in order to use for specific goals, for example define specific classes only for test environment, etc.

</p>

<p align="justify">

In order to apply profiling:

* For classes it uses `@Profile` annotation
* In `application.yml` file there are two solutions
    * uUse `---` and `spring.config.activate.on-profile=profile-name` property in yaml format
    * The `application.yml` should be included profile name with hyphen as separator, for
      example `application-test.yml`.
* In `application.properties` file there are two solutions
    * Use `spring.config.activate.on-profile=profile-name` property as separator
    * The `application.properties` should be included profile name with hyphen as separator, for
      example `application-test.yml`.

</p>

### How is it possible to specify active profile?

There are a few solutions for that as follows.

**JVM**

Execute the following command.

```shell
java -jar application.jar -Dspring.profiles.active=profile-name
```

**ENV**

Add the following variable as an environment variable.

```dotenv
SPRING_PROFILES_ACTIVE=profile-name
```

**Properties**

Add the following properties in `application.yml/properties`.

```properties
spring.config.activate.on-profile=profile-name
```

**Maven**

Add a profile as follows then execute Maven command include the name of profile.

```xml

<profile>
    <id>profile-name</id>
    <properties>
        <spring.profiles.active>profile-name</spring.profiles.active>
    </properties>
</profile>
```

```shell
mvn lifecycle -P profile-name
```

**Test Environment**

In order to activate a profile in test environment it uses `@ActiveProfiles("profile-name")` on top of test class.

```java

@ActiveProfiles("profile-name")
class Test {

}
```

## Description

In this sample there are two profiles named **dev** and **test**.

* if, do not set active profile, spring container scans all classes do not use `@Profile`
* if active profile set to **dev**, spring container scans all decorated classes with `@Profile("dev")` and classes do
  not use `@Profile`
* if active profile set to **test**, spring container scans all decorated classes with `@Profile("test")` and classes do
  not use `@Profile`

## Dependencies

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

## Prerequisites

* [Java 21](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)

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

##

**<p align="center"> [Top](#profile) </p>**