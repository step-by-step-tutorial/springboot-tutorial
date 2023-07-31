# <p align="center">RDBMS MySQL</p>

<p align="justify">

This tutorial is included [MySQL database](https://www.mysql.com/) configuration for test and none test environment.

Some parameters can be included in mysql URL connection are as follows.

* useUnicode=true
* useJDBCCompliantTimezoneShift=true
* useLegacyDatetimeCode=false
* serverTimezone=UTC
* pinGlobalTxToPhysicalConnection=TRUE

</p>

### URL

```yaml
url: jdbc:mysql://host:port/database-name?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&pinGlobalTxToPhysicalConnection=TRUE
```

## How To

### MySQL Service

Command to work with mysql service.

```shell
# connect
mysql -u root -p

# import database
mysql -u root -p < file.sql
# import data
mysql -u root -p db-name < file.sql

# backup database
mysqldump mysql -u root -p [–no-data] db-name [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] -databases db-name1 db-name2 ... [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] all-databases [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] db-name table-name1 table-name2 ... [–no-create-info] > file.sql
```

### SQL Commands

```mysql-sql
# create a database
CREATE DATABASE IF NOT EXISTS 'test_db' DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

# create a user
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';

# gran to a user and database
GRANT grant-name ON `db-name`.* TO 'db-user'@'db-host';
FLUSH PRIVILEGES;
exit;

# example to gran XA to a user
GRANT BINLOG_ADMIN, SYSTEM_VARIABLES_ADMIN ON *.* TO 'user'@'localhost';
GRANT XA_RECOVER_ADMIN ON *.* TO 'user'@'localhost';
GRANT ALL ON `test_db`.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
exit;
```

## Install MySQL on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    hostname: mysql
    restart: always
    ports:
      - "3306:3306"
    environment:
      - MYSQL_USER=user
      - MYSQL_PASSWORD=password
      - MYSQL_DATABASE=test_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./docker/mysql:/etc/mysql/conf.d"
  adminer:
    image: adminer
    container_name: adminer
    hostname: adminer
    restart: always
    ports:
      - "8080:8080"
```

Execute the `docker compose  up -d` command to install MySQL and Adminer.

<p align="justify">

In order to connect to MySQL via Adminer brows [http://localhost:8080](http://localhost:8080/) via web browser and use
the following properties in the login page.

</p>

```yaml
System: MySQL
Server: mysql:3306
Username: user
Password: password
Database: test_db
```

## Install MySQL on Kubernetes

### MySQL

Create the following file for installing MySQL.

**mysql-secrets.yml**

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysql-secrets
type: Opaque
data:
  mysql-root-password: cm9vdA==
  mysql-user: dXNlcg==
  mysql-password: cGFzc3dvcmQ=
```

**mysql-configmap.yml**

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mysql-configmap
data:
  mysql-database: test_db
# if you want to add privilege the user and database to support XA transactions you have to add the following queries
#  initdb.sql: |-
#    CREATE DATABASE IF NOT EXISTS `test_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
#    CREATE USER IF NOT EXISTS 'user' IDENTIFIED BY 'password';
#    GRANT BINLOG_ADMIN, SYSTEM_VARIABLES_ADMIN ON *.* TO 'user';
#    GRANT XA_RECOVER_ADMIN ON *.* TO 'user';
#    GRANT ALL ON `test_db`.* TO 'user';
#    FLUSH PRIVILEGES;
```

**mysql-pvc.yml**

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  labels:
    app: mysql
    tier: database
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

**mysql-deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  labels:
    app: mysql
    tier: database
spec:
  selector:
    matchLabels:
      app: mysql
      tier: database
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: mysql
        tier: database
    spec:
      containers:
        - image: mysql:8.0
          name: mysql
          imagePullPolicy: "IfNotPresent"
          env:
            - name: MYSQL_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-root-password
            - name: MYSQL_USER
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-user
            - name: MYSQL_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-password
            - name: MYSQL_DATABASE
              valueFrom:
                configMapKeyRef:
                  name: mysql-configmap
                  key: mysql-database
          ports:
            - name: mysql
              containerPort: 3306
          volumeMounts:
            - name: mysql-persistent-storage
              mountPath: /var/lib/mysql
      #            - name: mysql-initdb
      #              mountPath: /docker-entrypoint-initdb.d
      volumes:
        - name: mysql-persistent-storage
          persistentVolumeClaim:
            claimName: mysql-pvc
#        - name: mysql-initdb
#          configMap:
#            name: mysql-configmap
```

**mysql-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql
  labels:
    app: mysql
    tier: database
spec:
  selector:
    app: mysql
    tier: database
  type: NodePort
  ports:
    - port: 3306
      targetPort: 3306
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

### Apply the Files

Execute the following commands to install tools on Kubernetes.

```shell
kubectl apply -f ./kube/mysql-pvc.yml

kubectl apply -f ./kube/mysql-configmap.yml
# kubectl describe configmap mysql-configmap -n default

kubectl apply -f ./kube/mysql-secrets.yml
# kubectl describe secret mysql-secrets -n default
# kubectl get secret mysql-secrets -n default -o yaml

kubectl apply -f ./kube/mysql-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment mysql -n default

kubectl apply -f ./kube/mysql-service.yml
# kubectl get service -n default
# kubectl describe service mysql -n default

kubectl apply -f ./kube/adminer-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment adminer -n default

kubectl apply -f ./kube/adminer-service.yml
# kubectl get services -n default
# kubectl describe service adminer -n default

kubectl get all
```

<p align="justify">

If you want to connect to MySQL through application or Adminer from localhost through the web browser use the following
command and dashboard of Adminer is available with [http://localhost:8080](http://localhost:8080) URL.

</p>

```shell
# adminer
# http://localhost:8080
kubectl port-forward service/adminer 8080:80

# mysql
kubectl port-forward service/mysql 3306:3306
```

Use the following properties for Adminer.

```yaml
System: MySQL
Server: mysql:3306
Username: user
Password: password
Database: test_db
```

### Phpmyadmin

Also, there is another alternative for Adminer, named Phpmyadmin. Therefore, use the following instruction to install
that on Kubernetes.

**phpmyadmin-deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: phpmyadmin
  labels:
    app: phpmyadmin
spec:
  replicas: 1
  selector:
    matchLabels:
      app: phpmyadmin
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: phpmyadmin
    spec:
      containers:
        - name: phpmyadmin
          image: phpmyadmin/phpmyadmin
          imagePullPolicy: "IfNotPresent"
          ports:
            - containerPort: 80
          env:
            - name: PMA_HOST
              value: mysql
            - name: PMA_PORT
              value: "3306"
            - name: MYSQL_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-root-password
```

**phpmyadmin-service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: phpmyadmin
spec:
  type: NodePort
  selector:
    app: phpmyadmin
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
```

<p align="justify">

If you want to connect to Phpmyadmin from localhost through the web browser use the following command and dashboard of
Adminer is available with [http://localhost:8080](http://localhost:8080) URL.

</p>

```shell
# http://localhost:8080
kubectl port-forward service/phpmyadmin 8080:80
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
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
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
            <artifactId>mysql</artifactId>
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
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${DATABASE_NAME:test_db}
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: MYSQL
    database-platform: org.hibernate.dialect.MySQL8Dialect
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

**<p align="center"> [Top](#rdbms-mysql) </p>**