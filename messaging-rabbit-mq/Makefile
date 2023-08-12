createContainer:
	docker compose --file docker-compose.yml --project-name rabbitmq up -d

rebuildContainer:
	docker compose --file docker-compose.yml --project-name rabbitmq up --build -d

deleteContainer:
	docker rm rabbitmq --force

deleteImage:
	docker image rm rabbitmq:management

createKube:
	kubectl apply -f ./kube/rabbitmq-secrets.yml
	kubectl apply -f ./kube/rabbitmq-deployment.yml
	kubectl apply -f ./kube/rabbitmq-service.yml

deleteKube:
	kubectl delete all --all
	kubectl delete secrets rabbitmq-secrets

localhost:
	kubectl port-forward service/rabbitmq 15672:15672
