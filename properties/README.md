# <p align="center">Properties</p>

<p align="justify">

This sample is about working with yaml and properties file in Spring Boot. Also, the sample is using java `record` class
for using externalized properties.

</p>

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

### Properties File

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
@ConfigurationPropertiesScan({"properties bean package"})
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

@PropertySource(value = "classpath:sample.properties")
@ConfigurationProperties(prefix = "prefix")
@EnableConfigurationProperties
public record SampleProperties(int id, String name, String dateTime, String[] colors) {
}

```

### YAML File

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
@ConfigurationPropertiesScan({"properties bean package"})
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

@PropertySource(value = "classpath:sample.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "prefix")
@EnableConfigurationProperties
public record SampleYaml(int id, String name, String dateTime, String[] colors) {

}
```

## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
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

**<p align="center"> [Top](#properties) </p>**