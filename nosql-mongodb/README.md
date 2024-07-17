# <p align="center">Integration of Spring Boot And MongoDB</p>

<p align="justify">

This tutorial is about integration of Spring Boot and MongoDB.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [MongoDB](#mongodb)
* [MongoDB Use Cases](#mongodb-use-cases)
* [Install MongoDB on Docker](#install-mongodb-on-docker)
* [Install MongoDB on Kubernetes](#install-mongodb-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [MongoDB](https://www.mongodb.com)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

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

## MongoDB

<p align="justify">

MongoDB is a document database in NoSQL topic. For more information about MongoDB see
the [https://www.mongodb.com](https://www.mongodb.com).

</p>

## MongoDB Use Cases

List of use cases for MongoDB
[https://www.mongodb.com/solutions/use-cases](https://www.mongodb.com/solutions/use-cases).

* Artificial Intelligence
* Edge Computing
* Internet of Things
* Mobile
* Payments
* Serverless Development
* Single View
* Personalization
* Catalog
* Content Management
* Mainframe Modernization
* Gaming

## Install MongoDB on Docker

### Config File

If you set up MongoDB with username and password then you should enable `security.authorization` in `mongod.conf`
located in `/etc` directory of MongoDB machine. In oder to apply this change,
add `/path/to/mongod.conf:/etc/mongod.conf` volume to the docker compose file and put the customized config file in
the `/path/to/` directory in your machine.

In the following you can see the prepared config file and docker compose file.

[mongod.conf](./config/mongod.conf)

```textmate
#mongod.conf

# for documentation of all options, see:
#   http://docs.mongodb.org/manual/reference/configuration-options/

# Where and how to store data.
storage:
  dbPath: /var/lib/mongodb
#  engine:
#  wiredTiger:

# where to write logging data.
systemLog:
  destination: file
  logAppend: true
  path: /var/log/mongodb/mongod.log

# network interfaces
net:
  port: 27017
  bindIp: 127.0.0.1


# how the process runs
processManagement:
  timeZoneInfo: /usr/share/zoneinfo

security:
  authorization: enabled

#operationProfiling:

#replication:

#sharding:

## Enterprise-Only Options:

#auditLog:
```

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose File

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
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
    volumes:
      - "./config/mongod.conf:/etc/mongod.conf"
  mongo-express:
    image: mongo-express
    container_name: mongo-express
    hostname: mongo-express
    restart: always
    ports:
      - "8081:8081"
    environment:
      ME_CONFIG_OPTIONS_EDITORTHEME: "ambiance"
      ME_CONFIG_BASICAUTH: false
      ME_CONFIG_MONGODB_ADMINUSERNAME: root
      ME_CONFIG_MONGODB_ADMINPASSWORD: root
      ME_CONFIG_MONGODB_URL: mongodb://root:root@mongo:27017
```

### Apply Docker Compose File

Execute the following command to install MongoDB.

```shell
docker compose --file ./docker-compose.yml --project-name mongo up --build -d

```

### Mongo Express

In order to connect to MongoDB by Mongo Express through the web browser
open [http://localhost:8001](http://localhost:8001)
in the web browser.

## Install MongoDB on Kubernetes

Create the following files for installing MongoDB.

### MongoDB Kube Files

Use `echo -n secrets | base64` to encode the secrets, i.e, `echo -n root | base64`.

[mongo-secrets.yml](./kube/mongo-secrets.yml)

```yaml
# mongo-secrets.yml
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

[mongo-configmap.yml](./kube/mongo-configmap.yml)

```yaml
# mongo-configmap.yml 
apiVersion: v1
kind: ConfigMap
metadata:
  name: mongo-config
data:
  mongod.conf: |
    # for documentation of all options, see:
    #   http://docs.mongodb.org/manual/reference/configuration-options/

    # Where and how to store data.
    storage:
      dbPath: /var/lib/mongodb
    #  engine:
    #  wiredTiger:

    # where to write logging data.
    systemLog:
      destination: file
      logAppend: true
      path: /var/log/mongodb/mongod.log

    # network interfaces
    net:
      port: 27017
      bindIp: 127.0.0.1


    # how the process runs
    processManagement:
      timeZoneInfo: /usr/share/zoneinfo

    security:
      authorization: enabled

    #operationProfiling:

    #replication:

    #sharding:

    ## Enterprise-Only Options:

    #auditLog:

```

[mongo-pvc.yml](./kube/mongo-pvc.yml)

```yaml
# mongo-pvc.yml
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

[mongo-deployment.yml](./kube/mongo-deployment.yml)

```yaml
# mongo-deployment.yml
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
            - mountPath: /data/db
              name: mongo-data
            - mountPath: /etc/mongod.conf
              subPath: mongod.conf
              name: mongo-config
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
        - name: mongo-config
          configMap:
            name: mongo-config

```

[mongo-service.yml](./kube/mongo-service.yml)

```yaml
# mongo-service.yml
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

### MongoExpress Kube Files

[mongo-express-deployment.yml](./kube/mongo-express-deployment.yml)

```yaml
# mongo-express-deployment.yml
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
            - name: ME_CONFIG_BASICAUTH
              value: "false"
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

[mongo-express-service.yml](./kube/mongo-express-service.yml)

```yaml
# mongo-express-service.yml
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

### Apply Kube Files

You can apply Kubernetes files using the following commands.

```shell
kubectl apply -f ./kube/mongo-secrets.yml
kubectl apply -f ./kube/mongo-configmap.yml
kubectl apply -f ./kube/mongo-pvc.yml
kubectl apply -f ./kube/mongo-deployment.yml
kubectl apply -f ./kube/mongo-service.yml
kubectl apply -f ./kube/mongo-express-deployment.yml
kubectl apply -f ./kube/mongo-express-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to MongoDB from localhost through the application use the following command and MongoDB is available
on `localhost:27017`.

</p>

```shell
kubectl port-forward service/mongo 27017:27017
```

<p align="justify">

In order to connect to Mongo-express from localhost through the web browser use the following command and dashboard of
Mongo-express is available on [http://localhost:8081](http://localhost:8081) URL.

</p>

```shell
kubectl port-forward service/mongo-express 8081:8081
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

## How To Set up Spring Boot Test

### Dependency

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

### Java Config

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

## Appendix

### Makefile

```shell
docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name mongo up --build -d

docker-remove-container:
	docker rm mongo --force
	docker rm mongo-express --force

docker-remove-image:
	docker image rm mongo
	docker image rm mongo-express

kube-deploy:
	kubectl apply -f ./kube/mongo-secrets.yml
	kubectl apply -f ./kube/mongo-configmap.yml
	kubectl apply -f ./kube/mongo-pvc.yml
	kubectl apply -f ./kube/mongo-deployment.yml
	kubectl apply -f ./kube/mongo-service.yml
	kubectl apply -f ./kube/mongo-express-deployment.yml
	kubectl apply -f ./kube/mongo-express-service.yml

kube-remove:
	kubectl delete all --all
	kubectl delete secrets mongo-secrets
	kubectl delete secrets mongo-configmap
	kubectl delete persistentvolumeclaim mongo-pvc

kube-port-forward-mongo:
	kubectl port-forward service/mongo 27017:27017

kube-port-forward-mongo-express:
	kubectl port-forward service/mongo-express 8081:8081
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-mongodb) </p>**