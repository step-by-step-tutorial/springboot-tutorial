# <p align="center">Authentication With In-memory Implementation</p>

This tutorial is a quick setup for spring Security based on RBAC.

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/) (Optional)

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

#### Web Console

```yaml
URL: http://localhost:8080/login
Username: admin
Password: password
```

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/security-rbac-inmemory/doc/spring-security-inmemory.png"  height="30%" width="30%">

#### Check Status

```shell
# Access with user credentials
curl -v -u user:password http://localhost:8080/api/v1/users
```

```shell
# Access with admin credentials
curl -v -u admin:password http://localhost:8080/api/v1/users
```

## Install Application on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[Dockerfile](Dockerfile)

```dockerfile
FROM eclipse-temurin:21-jdk-alpine

ARG JAR_PATH=./target
ARG JAR_NAME=security-rbac-inmemory
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
  securityauthenticationinmemory:
    image: samanalishiri/securityauthenticationinmemory:latest
    build:
      context: .
      dockerfile: ./Dockerfile
    container_name: securityauthenticationinmemory
    hostname: securityauthenticationinmemory
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
docker compose --file ./docker-compose.yml --project-name securityauthenticationinmemory up --build -d
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
  name: securityauthenticationinmemory
spec:
  replicas: 1
  selector:
    matchLabels:
      app: securityauthenticationinmemory
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: securityauthenticationinmemory
    spec:
      containers:
        - name: securityauthenticationinmemory
          image: samanalishiri/securityauthenticationinmemory:latest
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
  name: securityauthenticationinmemory
spec:
  selector:
    app: securityauthenticationinmemory
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
kubectl port-forward service/securityauthenticationinmemory 8080:8080
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
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
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!--utils-->
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

### Application Properties

```yaml
server:
  address: ${APP_HOST:0.0.0.0}
  port: ${APP_PORT:8080}
```

### Java Config

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

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
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
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
	docker build -t samanalishiri/securityauthenticationinmemory:latest .

docker-deploy:
	docker run \
	--name securityauthenticationinmemory \
	-p 8080:8080 \
	-h securityauthenticationinmemory \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-itd samanalishiri/securityauthenticationinmemory:latest

docker-compose-deploy:
	docker compose --file ./docker-compose.yml --project-name securityauthenticationinmemory up --build -d

docker-remove-container:
	docker rm securityauthenticationinmemory --force

docker-remove-image:
	docker image rm samanalishiri/securityauthenticationinmemory:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/securityauthenticationinmemory 8080:8080
```

##

**<p align="center">[Top](#authentication-with-in-memory-implementation)</p>**