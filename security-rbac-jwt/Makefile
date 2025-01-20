build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/securityrbacjwt:latest .

docker-deploy:
	docker run \
	--name securityrbacjwt \
	-p 8080:8080 \
	-h securityrbacjwt \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-e APP_PROFILES=h2 \
	-itd samanalishiri/securityrbacjwt:latest

DockerComposeDeploy:
	docker compose --file ./docker-compose.yml --project-name securityrbacjwt up --build -d

docker-remove-container:
	docker rm securityrbacjwt --force
	docker rm pgadmin --force
	docker rm postgres --force

DockerRemoveImage:
	docker image rm samanalishiri/securityrbacjwt:latest
	docker image rm dpage/pgadmin4
	docker image rm postgres:13.9-alpine

kube-deploy:
	kubectl apply -f ./kube/postgres-pvc.yml
	kubectl apply -f ./kube/postgres-configmap.yml
	kubectl apply -f ./kube/postgres-secrets.yml
	kubectl apply -f ./kube/postgres-deployment.yml
	kubectl apply -f ./kube/postgres-service.yml
	kubectl apply -f ./kube/pgadmin-secrets.yml
	kubectl apply -f ./kube/pgadmin-deployment.yml
	kubectl apply -f ./kube/pgadmin-service.yml
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets postgres-secrets
	kubectl delete configMap postgres-configmap
	kubectl delete persistentvolumeclaim postgres-pvc
	kubectl delete secrets pgadmin-secrets
	kubectl delete ingress pgadmin

kube-port-forward-app:
	kubectl port-forward service/securityrbacjwt 8080:8080

kube-port-forward-postgres:
	kubectl port-forward service/postgres 5432:5432

kube-port-forward-pgadmin:
	kubectl port-forward service/pgadmin 8081:80
