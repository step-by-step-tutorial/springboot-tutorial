createContainer:
	docker compose --file docker-compose.yml --project-name kafka up -d

rebuildContainer:
	docker compose --file docker-compose.yml --project-name kafka up --build -d

deleteContainer:
	docker rm kafka --force
	docker rm zookeeper --force
	docker rm kafdrop --force

deleteImage:
	docker image rm confluentinc/cp-zookeeper:latest
	docker image rm confluentinc/cp-kafka:latest
	docker image rm obsidiandynamics/kafdrop:latest

createKube:
	kubectl apply -f ./kube/zookeeper-deployment.yml
	kubectl apply -f ./kube/zookeeper-service.yml
	kubectl apply -f ./kube/kafka-deployment.yml
	kubectl apply -f ./kube/kafka-service.yml
	kubectl apply -f ./kube/kafdrop-deployment.yml
	kubectl apply -f ./kube/kafdrop-service.yml

deleteKube:
	kubectl delete all --all

localhost:
	kubectl port-forward service/kafdrop-service 9000:9000
