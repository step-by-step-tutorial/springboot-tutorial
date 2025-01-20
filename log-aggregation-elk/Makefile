build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file docker-compose.yml --project-name dev-env up --build -d

DockerComposeDown:
	docker compose --file docker-compose.yml --project-name dev-env down

DockerRemoveImage:
	docker image rm samanalishiri/application:last

LocalPipeline:
	mvn clean package -DskipTests=true
	mvn test
	mvn spring-boot:run

DockerizedPipeline:
	mvn clean package -DskipTests=true
	mvn test
	docker compose --file docker-compose.yml --project-name dev-env up --build -d

E2eTest:
	curl http://localhost:8080