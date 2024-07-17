build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name artemis up --build -d

docker-remove-container:
	docker rm artemis --force

docker-remove-image:
	docker image rm apache/activemq-artemis:latest

kube-deploy:
	kubectl apply -f ./kube/artemis-deployment.yml
	kubectl apply -f ./kube/artemis-service.yml

kube-remove:
	kubectl delete all --all

kube-port-forward-artemis:
	kubectl port-forward service/artemis 8161:8161
