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

LocalPipeline:
	mvn clean package -DskipTests=true
	mvn test
	mvn spring-boot:run

DockerizedPipeline:
	mvn clean package -DskipTests=true
	mvn test
	docker compose --file docker-compose.yml --project-name dev-env up --build -d

e2e-test:
	docker cp example_data.sql mysql:/example_data.sql
	docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"