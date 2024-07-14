# <p align="center">Properties In Spring Boot</p>

<p align="justify">

This sample is about working with yaml and properties file in Spring Boot. Also, the sample is using java `record` class
for using externalized properties.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
    * [Dependencies](#dependencies)
    * [Implementation](#implementation)
        * [Based on Properties File](#based-on-properties-file)
        * [Based on YAML File](#based-on-yaml-file)

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

## How To Set up Spring Boot

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

## Implementation

There are two formats for properties, properties file and yaml file.

### Based on Properties File

```properties
prefix.number=1
prefix.string=value
prefix.date-time=yyyy-MM-ddTHH:mm:ss
prefix.array[0]=element 1
prefix.array[1]=element 2
prefix.array[2]=element 3
```

```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({"package of properties bean"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

```java

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:file.properties")
@ConfigurationProperties(prefix = "prefix")
@EnableConfigurationProperties
public record SampleProperties(int number, String string, String dateTime, String[] array) {
}

```

### Based on YAML File

For yml file you have to define a source factory.

```yaml
prefix:
  number: 1
  string: value
  date-time: yyyy-MM-ddTHH:mm:ss
  array:
    - element 1
    - element 2
    - element 3
```

```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({"package of properties bean"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

```java

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;

import static java.util.Objects.requireNonNull;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    @NonNull
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());

        return new PropertiesPropertySource(
                requireNonNull(resource.getResource().getFilename()),
                requireNonNull(factory.getObject())
        );
    }
}
```

```java

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:file.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "prefix")
@EnableConfigurationProperties
public record SampleYaml(int number, String string, String dateTime, String[] array) {

}
```

##

**<p align="center"> [Top](#properties-in-spring-boot) </p>**