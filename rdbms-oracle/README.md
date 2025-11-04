# <p align="center">Integration of Spring Boot And Oracle</p>

<p align="justify">
This tutorial demonstrates the integration of Spring Boot and [Oracle](https://www.oracle.com), including configuration for test and non-test environments.
</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [Oracle](#oracle)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

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

### Run

```shell
mvn spring-boot:start
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Stop

```shell
mvn spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
docker volume prune -f
```

## Dockerized

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker compose --file docker-compose.yml --project-name dev up --build -d
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev down
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## Kubernetes

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker build -t samanalishiri/application:latest . --no-cache
kubectl apply -f kube-dev.yml
```

### Check Status

```shell
kubectl get all -n dev
```

### Port Forwarding

```shell
# Oracle Database
kubectl port-forward service/oracle 1521:1521 -n dev
kubectl port-forward service/oracle 5500:5500 -n dev

# ORDS (SQL Developer Web)
kubectl port-forward service/ords 8080:8080 -n dev

# Adminer
kubectl port-forward service/adminer 8081:8081 -n dev

# Application
kubectl port-forward service/application 8080:8080 -n dev
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
kubectl delete all --all -n dev
kubectl delete secrets dev-credentials -n dev
kubectl delete configMap dev-config -n dev
kubectl delete persistentvolumeclaim oracle-pvc -n dev
kubectl delete persistentvolumeclaim ords-config-pvc -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* Oracle Enterprise Manager: [https://localhost:5500/em](https://localhost:5500/em)
* ORDS (SQL Developer Web): [http://localhost:8081/ords](http://localhost:8081/ords)
* Adminer: [http://localhost:8082](http://localhost:8082)

---

# Oracle

<details>
<summary>Show Oracle Training and ORDS Configuration</summary>

## Oracle

<p align="justify">
For more information about Oracle see the [Oracle](https://www.oracle.com).
</p>

### URL

```yaml
url: jdbc:oracle:thin:${ORACLE_HOST:localhost}:${ORACLE_PORT:1521}/${DATABASE_NAME:xepdb1}
```

### Sqlplus Command

```oraclesqlplus
-- execute sql file
@/tmp/ords_installer_privileges.sql testuser;
```

### SQL Command

```oracle
CREATE USER testuser IDENTIFIED BY password;
GRANT DBA TO testuser;
```

## Install Oracle on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: "3.8"

services:
  oracle:
    image: container-registry.oracle.com/database/express:21.3.0-xe
    container_name: oracle
    hostname: oracle
    restart: always
    ports:
      - "1521:1521"
      - "5500:5500"
    environment:
      ORACLE_PWD: password
    volumes:
      - "oracle_data:/opt/oracle/oradata"
  ords:
    image: container-registry.oracle.com/database/ords:latest
    container_name: ords
    hostname: ords
    depends_on:
      - oracle
    ports:
      - "8080:8080"
    volumes:
      - "ords_config:/etc/ords/config"
    entrypoint: [ "ords", "serve" ]

volumes:
  ords_config:
    driver: local
  oracle_data:
    driver: local
```

### Apply Docker Compose

Execute the following command to install Oracle.

```shell
docker compose --file ./docker-compose.yml --project-name oracle up --build -d
```

### Enterprise Manager

Open https://localhost:5500/em in web browser.

* user: system
* password: password
* container name: xepdb1

### Sqlplus

```shell
sqlplus username/password@//hostname:port/servicename

# Example
sqlplus sys/password@//localhost:1521/xepdb1
```

### ORDS (Sql Developer Web)

Open [http://localhost:8080/ords](http://localhost:8080/ords/sql-developer) but, you cannot log in to SQL Developer
before applying the following configuration.

Get `ords_installer_privileges.sql` file and then connect to the database.

```shell
# download from ords container
docker cp ords:/opt/oracle/ords/scripts/installer/ords_installer_privileges.sql ./
```

```shell
# upload to oracle container
docker cp ./ords_installer_privileges.sql oracle:/tmp
```

```shell
# connect to database
docker exec -it oracle sqlplus sys/password@//localhost:1521/xepdb1 as sysdba
```

You have to enable the ORDS schema for the target user.

```oraclesqlplus
CREATE USER testuser IDENTIFIED BY password;
GRANT DBA TO testuser;
@/tmp/ords_installer_privileges.sql testuser;
```

Apply configuration to the ORDS instance.

```shell
docker exec -it ords ords --config /etc/ords/config install
# [1] Basic (host name, port, service name)
# hostname: oracle
# port: 1521
# service name: xepdb1
# username: testuser
# password: password
# [A] Accept and Continue - Create configuration and Install ORDS in the database
```

```shell
docker restart ords
```

Connect to the Oracle database and execute the procedure.

```shell
docker exec -it oracle sqlplus testuser/password@//localhost:1521/xepdb1

```

```oracle-plsql
 begin
    ords.enable_schema(
        p_enabled => true,
        p_schema => 'testuser',
        p_url_mapping_type => 'BASE_PATH',
        p_url_mapping_pattern => 'testuser',
        p_auto_rest_auth => false
    );
    commit;
end;
/
```

Open [http://localhost:8080/ords/sql-developer](http://localhost:8080/ords/sql-developer) in web browser, then use
the following credentials to log in to the `Sql Developer Web`.

```yaml
Username: testuser
Password: password
```

</details>

**<p align="center"> [Top](#integration-of-spring-boot-and-oracle) </p>**