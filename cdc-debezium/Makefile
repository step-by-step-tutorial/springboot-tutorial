build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name cdc-debezium up --build -d

docker-compose-down:
	docker compose --file docker-compose.yml --project-name cdc-debezium down

docker-remove-image:
	docker image rm samanalishiri/cdcdebezium:last

local-pipeline:
	mvn clean package -DskipTests=true
	mvn test
	mvn spring-boot:run

dockerized-pipeline:
	mvn clean package -DskipTests=true
	mvn test
	docker compose --file docker-compose.yml --project-name cdc-debezium up --build -d

e2e-test:
	docker cp example_data.sql mysql:/example_data.sql
	docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"