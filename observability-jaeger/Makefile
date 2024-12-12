build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/observabilityjaeger:latest .

docker-deploy:
	docker run \
	--name observabilityjaeger \
	-p 8080:8080 \
	-h observabilityjaeger \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-e TRACING_HOST=http://jaeger:4318/v1/traces \
	-itd samanalishiri/observabilityjaeger:latest

docker-compose-deploy:
	docker compose --file ./docker-compose.yml --project-name observabilityjaeger up --build -d

docker-remove-container:
	docker rm observabilityjaeger --force

docker-remove-image:
	docker image rm samanalishiri/observabilityjaeger:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/observabilityjaeger 8080:8080