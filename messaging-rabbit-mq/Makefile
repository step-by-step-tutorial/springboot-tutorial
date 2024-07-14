build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-deploy:
	docker compose --file docker-compose.yml --project-name rabbitmq up -d

docker-rebuild-deploy:
	docker compose --file docker-compose.yml --project-name rabbitmq up --build -d

docker-remove-container:
	docker rm rabbitmq --force

docker-remove-image:
	docker image rm rabbitmq:management

kube-deploy:
	kubectl apply -f ./kube/rabbitmq-secrets.yml
	kubectl apply -f ./kube/rabbitmq-deployment.yml
	kubectl apply -f ./kube/rabbitmq-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets rabbitmq-secrets

kube-port-forward-rabbitmq:
	kubectl port-forward service/rabbitmq 15672:15672
