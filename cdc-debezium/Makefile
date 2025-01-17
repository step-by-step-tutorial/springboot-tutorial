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