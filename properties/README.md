# <p align="center">Properties</p>

This sample is about working with yml and properties in Spring Boot. Also, the sample is using java `record` class for
binging externalized properties.

There are two format for properties, properties file and yml file.

**Properties**

```properties
prefix.number=1
prefix.sttring=value
prefix.date-time=2022-01-01T08:00:00
prefix.array[0]=element 1
prefix.array[1]=element 2
prefix.array[2]=element 3
```

```java

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "prefix")
@PropertySource(value = "classpath:file-name.properties")
@EnableConfigurationProperties
public record DataProperties(int number, String string, String dateTime, String[] array) {
}
```

**YML**

For yml file you have to define a source factory.

```yaml
prefix:
  number: 1
  string: vlaue
  date-time: 2022-01-01T08:00:00
  array:
    - element 1
    - element 2
    - element 3
```

```java

import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
    YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(resource.getResource());

    return new PropertiesPropertySource(requireNonNull(resource.getResource().getFilename()),
        requireNonNull(factory.getObject()));
  }
}
```

```java


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "prefix")
@PropertySource(value = "classpath:file-name.yml", factory = YamlPropertySourceFactory.class)
@EnableConfigurationProperties
public record DataYaml(int number, String string, String dateTime, String[] array) {

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

**<p align="center"> [Top](#Properties) </p>**