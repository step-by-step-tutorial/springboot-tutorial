# <p align="center">RESTful Web Services</p>

This tutorial is about develop a RESTful web services includes API documentation based on Swagger.

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [API Design](#api-design)
* [Http Verbs](#http-verbs)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/) (Optional)
* [Postman CLI](https://learning.postman.com/docs/postman-cli/postman-cli-installation/) (Optional)

### Build

```shell
mvn clean package -DskipTests=true
```

### Test

```shell
mvn  test
```

### Run

```shell
mvn  spring-boot:run
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
postman collection run './e2eTest/postman/spring Boot Tutorial- restful-web-api.postman_collection.json' --environment './e2eTest/postman/spring Boot Tutorial- restful-web-api.postman_environment.json'
```

* To access actuator [http://localhost:8080/actuator](http://localhost:8080/actuator).
* To health check [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health).
* To access **swagger** ui,
  brows [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

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
        Create a record -> POST /api/v1/samples
        Read all records -> GET /api/v1/samples
        Read a record -> GET /api/v1/samples/{id}
        Update a record -> PUT /api/v1/samples/{id}
        Delete a record -> DELETE /api/v1/samples/{id}
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
the resource currently residing on the server should be modified to produce a new version. There is JSON library tp help
you named [Jsonpatch](https://jsonpatch.com/)

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

## Install Application on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[Dockerfile](Dockerfile)

```dockerfile
FROM eclipse-temurin:21-jdk-alpine

ARG JAR_PATH=./target
ARG JAR_NAME=restful-web-api
ARG JAR_VERSION=0.0.1-SNAPSHOT
ARG TARGET_PATH=/app
ENV APPLICATION=${TARGET_PATH}/application.jar
ENV PORT=8080

ADD ${JAR_PATH}/${JAR_NAME}-${JAR_VERSION}.jar ${TARGET_PATH}/application.jar

EXPOSE ${PORT}
ENTRYPOINT java -jar ${APPLICATION}
```

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: "3.8"

services:
  restfulwebapi:
    image: samanalishiri/restfulwebapi:latest
    build:
      context: .
      dockerfile: ./Dockerfile
    container_name: restfulwebapi
    hostname: restfulwebapi
    restart: always
    ports:
      - "8080:8080"
    environment:
      APP_HOST: "0.0.0.0"
      APP_PORT: "8080"

```

### Apply Docker Compose

Execute the following command to install Application.

```shell
docker compose --file ./docker-compose.yml --project-name restfulwebapi up --build -d
```

## Install Application on Kubernetes

Create the following files for installing Application.

### Kube Files

[app-deployment.yml](/kube/app-deployment.yml)

```yaml
#deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: restfulwebapi
spec:
  replicas: 1
  selector:
    matchLabels:
      app: restfulwebapi
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: restfulwebapi
    spec:
      containers:
        - name: restfulwebapi
          image: samanalishiri/restfulwebapi:latest
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
          env:
            - name: APP_HOST
              value: "0.0.0.0"
            - name: APP_PORT
              value: "8080"

```

[app-service.yml](/kube/app-service.yml)

```yaml
#service.yml
apiVersion: v1
kind: Service
metadata:
  name: restfulwebapi
spec:
  selector:
    app: restfulwebapi
  ports:
    - port: 8080
      targetPort: 8080

```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/app-deployment.yml
kubectl apply -f ./kube/app-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Application from localhost through the web browser use the following
URL [http://localhost:8080](http://localhost:8080).

</p>

```shell
kubectl port-forward service/securityrbac 8080:8080
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
    <!--spring-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <!--json-->
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
    <!--rest documentation-->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>
    <!--developer-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### Application Properties

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

## How To Set up Spring Boot Test

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
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

docker-build:
	docker build -t samanalishiri/restfulwebapi:latest .

docker-deploy:
	docker run \
	--name restfulwebapi \
	-p 8080:8080 \
	-h restfulwebapi \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-itd samanalishiri/restfulwebapi:latest

docker-compose-deploy:
	docker compose --file ./docker-compose.yml --project-name restfulwebapi up --build -d

docker-remove-container:
	docker rm restfulwebapi --force

docker-remove-image:
	docker image rm samanalishiri/restfulwebapi:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/restfulwebapi 8080:8080
```

##

**<p align="center">[Top](#restful-web-services)</p>**