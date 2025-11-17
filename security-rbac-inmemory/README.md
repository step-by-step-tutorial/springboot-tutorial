# <p align="center">Authentication With In-memory Implementation</p>

## <p align="center">Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

### Build

```shell
mvn clean compile -DskipTests=true
```

### Test

```shell
mvn  test
```

### Package

```shell
mvn package -DskipTests=true
```

### Run

```shell
mvn  spring-boot:start
```

### E2eTest

```shell
curl -X POST http://localhost:8080/login \
  -d "username=admin" \
  -d "password=password" \
  -v

```

### Stop

```shell
mvn  spring-boot:stop
```

## Dockerized

### Deploy

```shell
# docker command
mvn clean package
docker build -t samanalishiri/application:latest .
docker run \
	--name application \
	-p 8080:8080 \
	-h application \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-itd samanalishiri/application:latest
```

```shell
# docker compose
mvn clean package
docker compose --file docker-compose.yml --project-name dev up --build -d
```

### E2eTest

```shell
curl -X POST http://localhost:8080/login \
  -d "username=admin" \
  -d "password=password" \
  -v
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev down
docker rm application --force
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## Kubernetes

### Deploy

```shell
mvn clean package
docker build -t samanalishiri/application:latest . --no-cache
kubectl apply -f kube-dev.yml
```

### Check Status

```shell
kubectl get all -n dev
```

### Port Forwarding

```shell
kubectl port-forward service/application 8080:8080 -n dev
```

### E2eTest

```shell
curl -X POST http://localhost:8080/login \
  -d "username=admin" \
  -d "password=password" \
  -v
```

### Down

```shell
kubectl delete all --all -n dev
kubectl delete secrets dev-credentials -n dev
kubectl delete configMap dev-config -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* Login: [http://localhost:8080/login](http://localhost:8080/login).

```yaml
Username: admin
Password: password
```

##

**<p align="center">[Top](#authentication-with-in-memory-implementation)</p>**