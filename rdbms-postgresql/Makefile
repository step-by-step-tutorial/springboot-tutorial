build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name postgres up --build -d

docker-remove-container:
	docker rm postgres --force
	docker rm pgadmin --force
	docker rm adminer --force

docker-remove-image:
	docker image rm postgres:13.9-alpine
	docker image rm dpage/pgadmin4
	docker image rm adminer

kube-deploy:
	kubectl apply -f ./kube/postgres-pvc.yml
	kubectl apply -f ./kube/postgres-configmap.yml
	kubectl apply -f ./kube/postgres-secrets.yml
	kubectl apply -f ./kube/postgres-deployment.yml
	kubectl apply -f ./kube/postgres-service.yml
	kubectl apply -f ./kube/pgadmin-secrets.yml
	kubectl apply -f ./kube/pgadmin-deployment.yml
	kubectl apply -f ./kube/pgadmin-service.yml
	kubectl apply -f ./kube/adminer-deployment.yml
	kubectl apply -f ./kube/adminer-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets postgres-secrets
	kubectl delete configMap postgres-configmap
	kubectl delete persistentvolumeclaim postgres-pvc
	kubectl delete secrets pgadmin-secrets
	kubectl delete ingress pgadmin

kube-port-forward-postgres:
	kubectl port-forward service/postgres 5432:5432

kube-port-forward-pgadmin:
	kubectl port-forward service/pgadmin 8080:80

kube-port-forward-adminer:
	kubectl port-forward service/adminer 8080:8080
