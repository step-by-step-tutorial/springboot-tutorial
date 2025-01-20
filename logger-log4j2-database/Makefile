build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file ./docker-compose.yml --project-name mysql up --build -d

DockerComposeDown:
	docker compose --file ./docker-compose.yml --project-name mysql down

DockerRemoveImage:
	docker image rm mysql:8.0
	docker image rm lscr.io/linuxserver/mysql-workbench:latest
	docker image rm adminer
