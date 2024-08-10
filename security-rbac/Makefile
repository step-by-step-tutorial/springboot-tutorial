build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/securityrbac:latest .

docker-deploy:
	docker run \
	--name securityrbac \
	-p 8080:8080 \
	-h securityrbac \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-itd samanalishiri/securityrbac:latest

docker-compose-deploy:
	docker compose --file ./docker-compose.yml --project-name securityrbac up --build -d

docker-remove-container:
	docker rm securityrbac --force

docker-remove-image:
	docker image rm samanalishiri/securityrbac:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/securityrbac 8080:8080