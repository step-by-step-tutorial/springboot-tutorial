build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file docker-compose.yml --project-name kafka up --build -d

docker-remove-container:
	docker rm zookeeper --force
	docker rm kafka --force
	docker rm kafdrop --force

DockerRemoveImage:
	docker image rm docker.io/bitnami/zookeeper
	docker image rm docker.io/bitnami/kafka
	docker image rm obsidiandynamics/kafdrop:latest

kube-deploy:
	kubectl apply -f ./kube/zookeeper-deployment.yml
	kubectl apply -f ./kube/zookeeper-service.yml
	kubectl apply -f ./kube/kafka-deployment.yml
	kubectl apply -f ./kube/kafka-service.yml
	kubectl apply -f ./kube/kafdrop-deployment.yml
	kubectl apply -f ./kube/kafdrop-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-kafkadrop:
	kubectl port-forward service/kafdrop-service 9000:9000