# <p align="center">RDBMS PostgreSQL</p>

<p align="justify">

This tutorial is included [PostgreSQL database](https://www.postgresql.org/) configuration for test and none test
environment.

</p>

### URL

```yaml
url: jdbc:postgresql://host:port/database-name
```

### How To

```shell
# try to connect
sudo -u postgres psql postgres

# change password
alter user postgres with password 'password';

# exit from postgres database
type: \q

# how to create user?
sudo -u postgres createuser -D -A -P user-name
# example
sudo -u postgres createuser -D -A -P test_user

# how to create database?
sudo -u postgres createdb -O user-name db-name
# example
sudo -u postgres createdb -O test_user test_db

# how to run sql file?
sudo -u user-name psql -d db-name -f file-name.sql
# example
sudo -u test_user psql -d test_db -f test_db_schema.sql
```

## Install PostgreSQL on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

```yaml
version: "3.8"

services:
  postgresql:
    image: postgres:13.9-alpine
    container_name: postgresql
    hostname: postgresql
    restart: always
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: test_db
      PGDATA: /data/postgres
    volumes:
      - "./target/postgresql:/data/postgres"
  pgadmin:
    image: dpage/pgadmin4
    container_name: pgadmin
    hostname: pgadmin
    restart: always
    ports:
      - "8080:80"
    environment:
      PGADMIN_DEFAULT_EMAIL: pgadmin4@pgadmin.org
      PGADMIN_DEFAULT_PASSWORD: "password"
      PGADMIN_CONFIG_SERVER_MODE: "False"
    volumes:
      - "./target/pgadmin:/var/lib/pgadmin"
```

Execute the `docker compose  up -d` command to install PostgreSQL and pgadmin.

### PGADMIN

<p align="justify">

In order to connect to PostgreSQL via Pgadmin open [http://localhost:8080](http://localhost:8080/) through web browser
and use the following properties in the add-server popup.

</p>

```yaml
hostname: postgresql
port: 5432
Username: user
Password: password
```

### Adminer

Also, there is another alternative for Pgadmin for developing SQL named Adminer.

```yaml
adminer:
  image: adminer
  container_name: adminer
  hostname: adminer
  restart: always
  ports:
    - "8080:8080"
```

<p align="justify">

In order to connect to PostgreSQL via Adminer open [http://localhost:8080](http://localhost:8080/) through web browser
use the following properties in the login page.

</p>

```yaml
system: PostgreSQL
server: postgresql:5432
username: user
password: password
database: test_db
```

## Install PostgreSQL on Kubernetes

### PostgreSQL

Create the following file for installing PostgreSQL.

**postgres-secrets.yml**

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: postgres-secrets
type: Opaque
data:
  # value: user
  postgres-user: dXNlcg==
  # value: password
  postgres-password: cGFzc3dvcmQ=
```

**postgres-configmap.yml**

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: postgres-configmap
data:
  postgres-database: test_db
```

**postgres-pvc.yml**

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: postgres-pvc
  labels:
    app: postgres
    tier: database
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

**postgres-deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
  labels:
    app: postgres
    tier: database
spec:
  replicas: 1
  selector:
    matchLabels:
      app: postgres
      tier: database
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: postgres
        tier: database
    spec:
      containers:
        - name: postgres
          image: postgres:latest
          imagePullPolicy: "IfNotPresent"
          env:
            - name: POSTGRES_USER
              valueFrom:
                secretKeyRef:
                  name: postgres-secrets
                  key: postgres-user
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: postgres-secrets
                  key: postgres-password
            - name: POSTGRES_DB
              valueFrom:
                configMapKeyRef:
                  name: postgres-configmap
                  key: postgres-database
            - name: PGDATA
              value: /data/postgres
          ports:
            - name: postgres
              containerPort: 5432
          volumeMounts:
            - name: postgres-storage
              mountPath: /var/lib/postgresql/data
      volumes:
        - name: postgres-storage
          persistentVolumeClaim:
            claimName: postgres-pvc
```

**postgres-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: postgres
  labels:
    app: postgres
    tier: database
spec:
  selector:
    app: postgres
    tier: database

  ports:
    - port: 5432
      targetPort: 5432
```

### PGADMIN

**pgadmin-secrets.yml**

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: pgadmin-secrets
type: Opaque
data:
  # value: password
  pgadmin_default_password: cGFzc3dvcmQ=
```

**pgadmin-deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: pgadmin
spec:
  replicas: 1
  selector:
    matchLabels:
      app: pgadmin
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: pgadmin
    spec:
      containers:
        - name: pgadmin
          image: dpage/pgadmin4
          ports:
            - containerPort: 80
          env:
            - name: PGADMIN_DEFAULT_EMAIL
              value: pgadmin4@pgadmin.org
            - name: PGADMIN_DEFAULT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: pgadmin-secrets
                  key: pgadmin_default_password
            - name: PGADMIN_CONFIG_SERVER_MODE
              value: "False"
```

**pgadmin-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: pgadmin
spec:
  selector:
    app: pgadmin
  ports:
    - port: 80
      targetPort: 80
```

### Apply the Files

Execute the following commands to install tools on Kubernetes.

```shell
# ======================================================================================================================
# PostgreSQL
# ======================================================================================================================
kubectl apply -f ./kube/postgres-pvc.yml
# kubectl get pvc 
# kubectl describe pvc postgres-pvc

kubectl apply -f ./kube/postgres-configmap.yml
# kubectl describe configmap postgres-configmap -n default

kubectl apply -f ./kube/postgres-secrets.yml
# kubectl describe secret postgres-secrets -n default
# kubectl get secret postgres-secrets -n default -o yaml

kubectl apply -f ./kube/postgres-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment postgres -n default

kubectl apply -f ./kube/postgres-service.yml
# kubectl get service -n default
# kubectl describe service postgres -n default

# ======================================================================================================================
# Pgadmin
# ======================================================================================================================

kubectl apply -f ./kube/pgadmin-secrets.yml
# kubectl describe secret pgadmin-secrets -n default
# kubectl get secret pgadmin-secrets -n default -o yaml

kubectl apply -f ./kube/pgadmin-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment pgadmin -n default

kubectl apply -f ./kube/pgadmin-service.yml
# kubectl get services -n default
# kubectl describe service pgadmin -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================

kubectl get all
```

For connecting to PostgreSQL through application in localhost.

```shell
# postgres
kubectl port-forward service/postgres 5432:5432
```

<p align="justify">

To access to Pgadmin from localhost through the web browser use the following command and dashboard of Pgadmin is
available with [http://localhost:8080](http://localhost:8080) URL.

</p>

```shell
# pgadmin
# http://localhost:8080
kubectl port-forward service/pgadmin 8080:80
```

Use the following properties in the add-server popup.

```yaml
hostname: postgresql
port: 5432
Username: user
Password: password
```

### Adminer

**adminer-deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: adminer
spec:
  replicas: 1
  selector:
    matchLabels:
      app: adminer
  template:
    metadata:
      labels:
        app: adminer
    spec:
      containers:
        - name: adminer
          image: adminer:latest
          ports:
            - containerPort: 8080
```

**adminer-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: adminer
spec:
  selector:
    app: adminer
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
```

Execute the following commands to install tools on Kubernetes.

```shell
# ======================================================================================================================
# Adminer
# ======================================================================================================================
kubectl apply -f ./kube/adminer-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment adminer -n default

kubectl apply -f ./kube/adminer-service.yml
# kubectl get services -n default
# kubectl describe service adminer -n default

kubectl get all
```

<p align="justify">

For connecting to Adminer from localhost through the web browser use the following command and dashboard of Adminer is
available with [http://localhost:8080](http://localhost:8080) URL.

</p>

```shell
# adminer
# http://localhost:8080
kubectl port-forward service/adminer 8080:8080

```

Use the following properties for Adminer.

```yaml
system: PostgreSQL
server: postgres:5432
username: user
password: password
database: test_db
```

## How To Config Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```

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
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### Spring Boot Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_NAME:user}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:postgresql://${POSTGRESQL_HOST:localhost}:${POSTGRESQL_PORT:5432}/${DATABASE_NAME:test_db}
    driver-class-name: org.postgresql.Driver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: POSTGRESQL
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    defer-datasource-initialization: true
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      javax:
        persistence:
          create-database-schemas: true
      hibernate:
        generate_statistics: true
        format_sql: true
        naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
        default_schema: ${DATABASE_SCHEMA:sample}
---
spring:
  config:
    activate:
      on-profile: test
  data:
    jpa:
      hibernate:
        ddl-auto: create
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
mvn  test
```

## Run

```bash
mvn  spring-boot:run
```

##

**<p align="center"> [Top](#rdbms-postgresql) </p>**