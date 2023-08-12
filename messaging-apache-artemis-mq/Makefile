createContainer:
	docker compose --file docker-compose.yml --project-name artemis up -d

rebuildContainer:
	docker compose --file docker-compose.yml --project-name artemis up --build -d

deleteContainer:
	docker rm artemis --force

deleteImage:
	docker image rm apache/activemq-artemis:latest

createKube:
	kubectl apply -f ./kube/artemis-deployment.yml
	kubectl apply -f ./kube/artemis-service.yml

deleteKube:
	kubectl delete all --all

localhost:
	kubectl port-forward service/artemis 8161:8161