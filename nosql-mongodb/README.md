# <p align="center">NoSQL MongoDB</p>

<p align="justify">

This tutorial is included [MongoDB](https://www.mongodb.com/) configuration for test and none test environment.

</p>

## Install MongoDB on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

```yaml
version: '3.8'

services:

  mongo:
    image: mongo
    container_name: mongo
    hostname: mongo
    restart: always
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: root
  mongo-express:
    image: mongo-express
    container_name: mongo-express
    hostname: mongo-express
    restart: always
    ports:
      - "8081:8081"
    environment:
      ME_CONFIG_MONGODB_ADMINUSERNAME: root
      ME_CONFIG_MONGODB_ADMINPASSWORD: root
      ME_CONFIG_MONGODB_URL: mongodb://root:root@mongo:27017
```

Execute the `docker compose  up -d` command to install MongoDB and Mongo Express.

```shell
# full command
docker compose --file ./docker-compose.yml --project-name mongo up --build -d

```

### Mongo Express

In order to connect to MongoDB by Mongo Express through the web browser
open [http://localhost:8001/](http://localhost:8001/)
in the web browser.

## Install MongoDB on Kubernetes

### MongoDB

Create the following files for installing MongoDB.

**mongo-pvc.yml**

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mongo-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

**mongo-secrets.yml**

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mongo-secrets
type: Opaque
data:
  # value: root
  username: cm9vdA==
  # value: root
  password: cm9vdA==
```

**mongo-deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mongo
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mongo
  template:
    metadata:
      labels:
        app: mongo
    spec:
      containers:
        - name: mongo
          image: mongo:latest
          ports:
            - containerPort: 27017
          volumeMounts:
            - name: mongo-data
              mountPath: /data/db
          env:
            - name: MONGO_INITDB_ROOT_USERNAME
              valueFrom:
                secretKeyRef:
                  name: mongo-secrets
                  key: username
            - name: MONGO_INITDB_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mongo-secrets
                  key: password
      volumes:
        - name: mongo-data
          persistentVolumeClaim:
            claimName: mongo-pvc

```

**mongo-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: mongo
spec:
  selector:
    app: mongo
  ports:
    - port: 27017
      targetPort: 27017
```

### Mongo Express

**mongo-express-deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mongo-express
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mongo-express
  template:
    metadata:
      labels:
        app: mongo-express
    spec:
      containers:
        - name: mongo-express
          image: mongo-express:latest
          ports:
            - containerPort: 8081
          env:
            - name: ME_CONFIG_MONGODB_ADMINUSERNAME
              valueFrom:
                secretKeyRef:
                  name: mongo-secrets
                  key: username
            - name: ME_CONFIG_MONGODB_ADMINPASSWORD
              valueFrom:
                secretKeyRef:
                  name: mongo-secrets
                  key: password
            - name: ME_CONFIG_MONGODB_URL
              value: "mongodb://$(ME_CONFIG_MONGODB_ADMINUSERNAME):$(ME_CONFIG_MONGODB_ADMINPASSWORD)@mongo:27017"
```

**mongo-express-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: mongo-express
spec:
  selector:
    app: mongo-express
  ports:
    - port: 8081
      targetPort: 8081
```

### Apply Configuration Files

Execute the following commands to install the tools on Kubernetes.

```shell
# ======================================================================================================================
# MongoDB
# ======================================================================================================================
kubectl apply -f ./kube/mongo-pvc.yml
# kubectl get pvc
# kubectl describe pvc mongo-pvc

kubectl apply -f ./kube/mongo-secrets.yml
# kubectl describe secret mongo-secrets -n default
# kubectl get secret mongo-secrets -n default -o yaml

kubectl apply -f ./kube/mongo-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment mongo -n default

kubectl apply -f ./kube/mongo-service.yml
# kubectl get service -n default
# kubectl describe service mongo -n default

# ======================================================================================================================
# Mongo Express
# ======================================================================================================================
kubectl apply -f ./kube/mongo-express-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment mongo-express -n default

kubectl apply -f ./kube/mongo-express-service.yml
# kubectl get services -n default
# kubectl describe service mongo-express -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all
```

For connecting to MongoDB through application on localhost it should be executed the following command.

```shell
# mongo
kubectl port-forward service/mongo 27017:27017
```

<p align="justify">

In order to connect to Mongo Express from localhost through the web browser use the following command and dashboard of
Mongo Express is available on [http://localhost:8001](http://localhost:8001) URL.

</p>

```shell
# mongo-express
# http://localhost:8001
kubectl port-forward service/mongo-express 8001:8001
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
</dependencies>
```

### Application Properties

```yaml
spring:
  data:
    mongodb:
      username: root
      password: root
      host: ${MONGO_DB_HOST:localhost}
      port: ${MONGO_DB_PORT:27017}
      database: ${MONGO_DB_NAME:test_db}
      authentication-database: admin
```

### Java Config

```java

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "repository-package")
public class MongoDbConfig {
}

```

## How To Set up Test

### Test Dependency

```xml

<project>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.testcontainers</groupId>
                <artifactId>testcontainers-bom</artifactId>
                <version>1.18.3</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>mongodb</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### Java Config for Test

```java

@Testcontainers
@DataMongoTest
class SampleRepositoryTest {

    @Container
    static final MongoDBContainer MONGODB = new MongoDBContainer("mongo:5.0.16");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGODB::getReplicaSetUrl);
    }

    @BeforeAll
    static void setUp() {
        MONGODB.start();
    }

    @AfterAll
    static void tearDown() {
        MONGODB.stop();
    }
}
```

## Prerequisites

* [Java 17](https://www.oracle.com/de/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)

## Build

```bash
mvn clean package -DskipTests=true 
```

## Test

```bash
mvn test
```

## Run

```bash
mvn  spring-boot:run
```

##

**<p align="center"> [Top](#nosql-mongodb) </p>**