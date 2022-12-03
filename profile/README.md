# <p align="center">Profile</p>

This sample is about working with profiles in Spring Boot. The Profiles provide a possibility to categorized classes in
order to use for specific goals, for example define specific classes only for test environment, etc.

### How is it possible to specify active profile?

#### JVM

```shell
java -jar application.jar -Dspring.profiles.active=profile-name
```

#### ENV

```dotenv
SPRING_PROFILES_ACTIVE=profile-name
```

#### Properties

```properties
spring.config.activate.on-profile=profile-name
```

#### Maven

```xml

<profile>
    <id>profile-name</id>
    <properties>
        <spring.profiles.active>profile-name</spring.profiles.active>
    </properties>
</profile>
```

```shell
mvn clean package -P profile-name
```

#### Test Environment

Use `@ActiveProfiles("profile-name")` on top of test class.

```java
// sample
@ActiveProfiles("profile-name")
class Test {

}
```

### Description

In this sample there are two profiles named **dev** and **test**.

* if, do not set active profile, spring container scans all classes do not use `@Profile`
* if active profile set to **dev**, spring container scans all decorated classes with `@Profile("dev")` and classes do
  not
  use `@Profile`
* if active profile set to **test**, spring container scans all decorated classes with `@Profile("test")` and classes do
  not use `@Profile`

## Prerequisites

* Java 17
* Maven 3

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

**<p align="center"> [Top](#Profile) </p>**