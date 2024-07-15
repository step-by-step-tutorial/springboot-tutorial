build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-deploy:
	docker compose --file docker-compose.yml --project-name oracle up -d

docker-rebuild-deploy:
	docker compose --file docker-compose.yml --project-name oracle up --build -d

docker-remove-container:
	docker rm oracle --force
	docker rm ords --force

docker-remove-image:
	docker image rm container-registry.oracle.com/database/express:21.3.0-xe
	docker image rm container-registry.oracle.com/database/ords:latest

kube-deploy:
	kubectl apply -f ./kube/oracle-pvc.yml
	kubectl apply -f ./kube/oracle-deployment.yml
	kubectl apply -f ./kube/oracle-service.yml
	kubectl apply -f ./kube/ords-pvc.yml
	kubectl apply -f ./kube/ords-deployment.yml
	kubectl apply -f ./kube/ords-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-oracle:
	kubectl port-forward service/oracle 1521:1521

kube-port-forward-ords:
	kubectl port-forward service/ords 8080:8080
