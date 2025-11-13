# <p align="center">Spring Boot Profiling</p>

<p style="text-align: justify;">

This tutorial is about working with profiles in the Spring Boot. The Profiles provide a possibility to categorize
classes to use for specific goals, for example, define specific classes only for test environment, etc.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Define Profile](#define-profile)
    * [Active Profile](#active-profile)
    * [Test Environment](#test-environment)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)

### Build

```shell
mvn validate clean compile 
```

### Test

```shell
mvn test
```

### Package

```shell
mvn package -DskipTests=true
```

## Define Profile

<p style="text-align: justify;">

To apply profiling:

* For classes it uses `@Profile` annotation
* In `application.yml` file there are two solutions
    * Use `---` and `spring.config.activate.on-profile=profile-name` property in YAML format
    * The `application.yml` should be included profile name with hyphen as the separator, for
      example `application-test.yml`.
* In `application.properties` file there are two solutions
    * Use `spring.config.activate.on-profile=profile-name` property as separator
    * The `application.properties` should be included profile name with hyphen as separator, for
      example `application-test.yml`.

</p>

### Active Profile

There are a few solutions for that as follows.

#### JVM

Execute the following command.

```shell
java -jar application.jar -Dspring.profiles.active=profile-name
```

#### ENV

Add the following variable as an environment variable.

```dotenv
SPRING_PROFILES_ACTIVE=profile-name
```

#### Properties

Add the following properties in `application.yml/properties`.

```properties
spring.profiles.active=profile-name
```

#### Maven

Add a profile as follows in the pom.xml file.

```xml

<profile>
    <id>profile-name</id>
    <properties>
        <spring.profiles.active>profile-name</spring.profiles.active>
    </properties>
</profile>
```

Execute Maven command includes the name of the profile.

```shell
mvn lifecycle -P profile-name
```

### Test Environment

To activate a profile in test environment it uses `@ActiveProfiles("profile-name")` on top of test class and the profile
must be already defined.

```java

@ActiveProfiles("profile-name")
class Test {

}
```

##

**<p align="center"> [Top](#spring-boot-profiling) </p>**