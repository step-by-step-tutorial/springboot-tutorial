build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file docker-compose.yml --project-name redis up --build -d

docker-remove-container:
	docker rm redis --force
	docker rm redisinsight --force
	docker rm commander --force

DockerRemoveImage:
	docker image rm redis:latest
	docker image rm redislabs/redisinsight:latest
	docker image rm rediscommander/redis-commander:latest

kube-deploy:
	kubectl apply -f ./kube/redis-deployment.yml
	kubectl apply -f ./kube/redis-service.yml
	kubectl apply -f ./kube/redisinsight-deployment.yml
	kubectl apply -f ./kube/redisinsight-service.yml
	kubectl apply -f ./kube/commander-deployment.yml
	kubectl apply -f ./kube/commander-service.yml

kube-remove:
	kubectl delete all --all

kube-port-forward-redis:
	kubectl port-forward service/redis 6379:6379

kube-port-forward-redisinsight:
	kubectl port-forward service/redisinsight 5540:5540

kube-port-forward-commander:
	kubectl port-forward service/commander 8081:8081
