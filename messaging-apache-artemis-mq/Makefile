docker-deploy:
	docker compose --file docker-compose.yml --project-name artemis up -d

docker-rebuild-deploy:
	docker compose --file docker-compose.yml --project-name artemis up --build -d

docker-remove-container:
	docker rm artemis --force

docker-remove-image:
	docker image rm apache/activemq-artemis:latest

kube-deploy:
	kubectl apply -f ./kube/artemis-deployment.yml
	kubectl apply -f ./kube/artemis-service.yml

kube-delete:
	kubectl delete all --all

kube-bind-to-localhost:
	kubectl port-forward service/artemis 8161:8161
