build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/securityauthenticationinmemory:latest .

docker-deploy:
	docker run \
	--name securityauthenticationinmemory \
	-p 8080:8080 \
	-h securityauthenticationinmemory \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-itd samanalishiri/securityauthenticationinmemory:latest

DockerComposeDeploy:
	docker compose --file ./docker-compose.yml --project-name securityauthenticationinmemory up --build -d

docker-remove-container:
	docker rm securityauthenticationinmemory --force

DockerRemoveImage:
	docker image rm samanalishiri/securityauthenticationinmemory:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/securityauthenticationinmemory 8080:8080