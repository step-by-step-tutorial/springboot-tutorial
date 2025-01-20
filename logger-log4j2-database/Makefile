build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file ./docker-compose.yml --project-name dev-env up --build -d

DockerComposeDown:
	docker compose --file ./docker-compose.yml --project-name dev-env down

LocalPipeline:
	mvn clean package -DskipTests=true
	mvn test
	mvn spring-boot:run

DockerizedPipeline:
	mvn clean package -DskipTests=true
	docker compose --file docker-compose.yml --project-name dev-env up --build -d

e2e-test:
	docker exec -i mysql mysql -h localhost -u user -ppassword -e "USE tutorial_db; SELECT * FROM LOG_TABLE;"