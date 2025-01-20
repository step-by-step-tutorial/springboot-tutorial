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

CloudNativePipeline:
	docker build -t samanalishiri/application:latest .
	kubectl apply -f ./kube/mysql.yml
	kubectl apply -f ./kube/kafka.yml
	kubectl apply -f ./kube/debezium.yml
	kubectl apply -f ./kube/redis.yml
	kubectl apply -f ./kube/application.yml

CloudNativeDown:
	kubectl delete all --all
	kubectl delete secrets mysql-secrets
	kubectl delete configMap mysql-configmap
	kubectl delete persistentvolumeclaim mysql-pvc
	docker image rm samanalishiri/application:latest

E2eTest:
	docker cp example_data.sql mysql:/example_data.sql
	docker exec -it mysql mysql -u root -proot -h localhost -e "SOURCE /example_data.sql"