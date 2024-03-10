# <p align="center">RESTful Web Services</p>

This tutorial is about develop a RESTful web services includes API documentation based on Swagger.

## API Design

To develop an efficient and standard-conforming RESTful web API, keep in mind the following tips and recommendations:

1. Integrating a resource identifier prefix: When constructing your URIs, it is advised to use a distinctive keyword as
   a prefix. This notifies the API user that the paths following this prefix are part of the URI space. I recommend
   using `api` as this prefix for clarity and conventionality. An example URI would be something like `/api/...`.

2. Versioning your API: Incorporating versioning in your API guarantees its scalability and backward compatibility. This
   is commonly implemented by including the version number, i.e., v1, v2, etc., in the URI. For instance, `/api/v1/...`.

3. Naming conventions for endpoint categories: All endpoints corresponding to a particular category should commence with
   a plural noun (like `users`, `orders`, `products`, etc.). This practice maintains uniformity and provides intuitive
   context to the API end-users.

4. CRUD operation tailing on category roots: CRUD operations should rely on HTTP verbs rather than path
   tails for differentiated actions. For example, the base URI (`/api/v1/samples`) should accommodate all CRUD
   operations by varying the HTTP verbs and not the endpoint path:

    ```text
    Base URI: /api/v1/samples
        Create Record -> POST /api/v1/samples
        Read All Records -> GET /api/v1/samples
        Update Record -> PUT /api/v1/samples/{id}
        Delete Record -> DELETE /api/v1/samples/{id}
    ```

5. Adopting comprehensive READ operations: The GET operation for the base URI should return the complete list of objects
   under that category, facilitating easy access and reviewing for the client.

6. Single record retrieval: To return a specific instance of the object, the client should be able to access it via the
   object's unique identifier passed in the URI, like `GET /api/v1/samples/{id}`.

7. Employing PUT for complete record updates: Use PUT when you require an 'update with replacement' strategy. The entire
   content supplied replaces the existing record, so you need to provide full details in the request body.

8. Adopting PATCH for partial updates: If only a portion of a record has to be updated, the PATCH method enables it.
   Unlike PUT, PATCH permits you to submit only the changed portions of the record in the request payload. This method
   is ideal for small and efficient updates.

## HTTP Verbs

### POST:

This HTTP verb is used to submit data to be processed by the identified resource. The submitted data is included in the
body of the HTTP request.

```java
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostMapping("/uri")
public ResponseEntity<ID> create(@RequestBody DTO dto) {
}
```

### GET:

This HTTP verb is used to retrieve information from the identified resource.

```java
import org.springframework.web.bind.annotation.GetMapping;

@GetMapping("/uri/{id}")
public ResponseEntity<DTO> get(@PathVariable Long id) {
}
```

### PUT:

This HTTP verb is used to update the current representation of the resource with the new data provided.

```java
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PutMapping("/uri/{id}")
public void update(@PathVariable Long id, @RequestBody DTO dto) {
}
```

### PATCH:

This HTTP verb is used to partially update a resource. The request body contains a set of instructions describing how
the resource currently residing on the server should be modified to produce a new version.

```java
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PatchMapping("/uri/{id}")
public void partialUpdate(@PathVariable Long id, @RequestBody DTO dto) {
}
```

### DELETE:

This HTTP verb is used to delete a resource identified by a URI.

```java
import org.springframework.web.bind.annotation.DeleteMapping;

@DeleteMapping("/uri/{id}")
public void delete(@PathVariable Long id) {
}
``` 

### HEAD:

This HTTP verb is similar to the GET verb, but it only gets the headers and not the response body. This can be used to
check if a resource is available or to get the headers without the actual data. Spring does not provide a specific
annotation for a HEAD method. But you can indicate the method type in the @RequestMapping annotation:

 ```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/uri/{id}", method = RequestMethod.HEAD)
public ResponseEntity<String> head(@PathVariable Long id) {
}
``` 

### OPTIONS:

This HTTP verb is used to describe the communication options for the target resource. Again, Spring does not provide a
specific annotation for an OPTIONS method. But you can indicate the method type in the @RequestMapping annotation:

 ```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/uri", method = RequestMethod.OPTIONS)
public ResponseEntity<String> options() {
}
```

## How To Config Spring Boot

### Dependencies

#### Main Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

```

#### JSON Dependencies

In order to support JSON, the following dependencies must be added.

```xml

<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-annotations</artifactId>
    </dependency>
</dependencies>
```

#### Swagger Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>
</dependencies>
```

### Spring Boot Properties

```yaml
server:
  address: ${APP_HOST:0.0.0.0}
  port: ${APP_PORT:8080}
```

### Java Config

```java
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Spring Boot Tutorial", description = "RESTful web service"),
        servers = @Server(url = "http://localhost:8080")
)
public class OpenApiConfig {
}
```

## Prerequisites

* [Java 21](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)

## Build

```bash
mvn clean package -DskipTests=true
```

## Test

### Unit Test

```bash
mvn  test
```

### E2E Test

#### Command Line

```shell
# Unix/Linux
./e2eTest/shell/e2etest.sh
```

```shell
# Windows
./e2eTest/shell/e2etest.bat
```

#### Postman CLI

```shell
postman collection run './e2eTest/postman/spring Boot Tutorial- restful-web-api.postman_collection.json' --environment './e2eTest/postman/Spring Boot Tutorial.postman_environment.json'
```

## Run

```shell
mvn  spring-boot:run
```

To access actuator [http://localhost:8080/actuator](http://localhost:8080/actuator)
To health check [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
To access **swagger** ui
brows [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

##

**<p align="center">[Top](#restful-web-services)</p>**