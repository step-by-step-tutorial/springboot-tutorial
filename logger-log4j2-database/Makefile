build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-compose-deploy:
	docker compose --file ./docker-compose.yml --project-name mysql up --build -d

docker-remove-container:
	docker rm mysql --force
	docker rm mysql-workbench- --force
	docker rm adminer --force

docker-remove-image:
	docker image rm mysql:8.0
	docker image rm lscr.io/linuxserver/mysql-workbench:latest
	docker image rm adminer
