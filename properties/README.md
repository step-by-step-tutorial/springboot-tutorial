# <p align="center">Properties In Spring Boot</p>

<p style="text-align: justify;">

This sample is about working with YAML and properties file in Spring Boot. Also, the sample is using java `record` class
for using externalized properties.

</p>

## <p align="center">Table of Content</p>

* [Getting Started](#getting-started)
* [Implementation](#implementation)
  * [Properties](#properties)
  * [Java Code](#java-code)

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

## Implementation

There are two formats for properties, properties file and YAML file.

### Properties

```properties
prefix.number=1
prefix.string=value
prefix.date-time=yyyy-MM-ddTHH:mm:ss
prefix.array[0]=element 1
prefix.array[1]=element 2
prefix.array[2]=element 3
```

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

### Java Code

```java
package com.tutorial.springboot.properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({"com.tutorial.springboot.properties;"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

```java
// for using externalized properties
package com.tutorial.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:file.properties)
@ConfigurationProperties(prefix = "prefix")
@EnableConfigurationProperties
public record SampleProperties(int number, String string, String dateTime, String[] array) {
}
```

```java
// for using externalized properties with YAML file
package com.tutorial.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:file.yml", encoding = "UTF-8", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "prefix")
@EnableConfigurationProperties
public record SampleProperties(int number, String string, String dateTime, String[] array) {
}
```

```java
// for using externalized properties with YAML file
package com.tutorial.springboot.properties;

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

##

**<p align="center">[Top](#properties-in-spring-boot)</p>**