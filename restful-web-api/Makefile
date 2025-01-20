build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/restfulwebapi:latest .

docker-deploy:
	docker run \
	--name restfulwebapi \
	-p 8080:8080 \
	-h restfulwebapi \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-itd samanalishiri/restfulwebapi:latest

DockerComposeDeploy:
	docker compose --file ./docker-compose.yml --project-name restfulwebapi up --build -d

docker-remove-container:
	docker rm restfulwebapi --force

DockerRemoveImage:
	docker image rm samanalishiri/restfulwebapi:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/restfulwebapi 8080:8080