# <p align="center">Authentication With In-memory Users</p>

This tutorial is about develop a RESTful web services includes API documentation based on Swagger.

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

```bash
mvn clean package -DskipTests=true
```

### Test

```bash
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
Password: admin
```

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/security-authentication-inmemory/doc/spring-security-inmemory.png"  height="30%" width="30%">

#### Check Status

```shell
# Access with user credentials
curl -v -u user:password http://localhost:8080
```

```shell
# Access with admin credentials
curl -v -u admin:admin http://localhost:8080
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        var admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
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

**<p align="center">[Top](#authentication-with-in-memory-users)</p>**