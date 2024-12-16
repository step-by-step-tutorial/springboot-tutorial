```shell
mvn clean package -DskipTests=true 
```

```shell
mvn test
```

```shell
docker compose --file ./docker-compose.yml --project-name grafana up --build -d
```

```shell
curl http://localhost:8080/api/v1/application/status
```
Actuator: http://localhost:8080/actuator
Grafana: http://localhost:3000
Prometheus: http://localhost:9090
            http://localhost:9090/targets
Tempo: http://localhost:3110
Loki: http://localhost:3100


### Grafana Stack
* k6 service running a load test against the above application.
* Tempo service for storing and querying trace information.
* Loki service for storing and querying log information.
* Mimir service for storing and querying metric information.
* Pyroscope service for storing and querying profiling information.
* Beyla services for watching the four-service application and automatically generating signals.
* Grafana service for visualising observability data.
* Grafana Alloy service for receiving traces and producing metrics and logs based on these traces.

Observe the Spring Boot application with three pillars of observability on Grafana:

* Traces with Tempo and OpenTelemetry Instrumentation for Java
* Metrics with Prometheus, Spring Boot Actuator, and Micrometer
* Logs with Loki and Logback