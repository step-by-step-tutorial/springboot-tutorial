docker-compose-deploy:
	mvn -f security-oauth2-server/pom.xml clean package -DskipTests=true
	mvn -f security-oauth2-client/pom.xml clean package -DskipTests=true
	mvn -f security-oauth2-server/pom.xml test
	mvn -f security-oauth2-client/pom.xml test
	docker compose --file ./docker-compose.yml --project-name securityoauth2 up --build -d

docker-remove-container:
	docker rm securityoauth2server --force
	docker rm securityoauth2client --force
	docker rm nginx --force
	docker rm pgadmin --force
	docker rm postgres --force

docker-remove-image:
	docker image rm samanalishiri/securityoauth2server:latest
	docker image rm samanalishiri/securityoauth2client:latest

kube-deploy:
	kubectl apply -f ./kube/postgres-pvc.yml
	kubectl apply -f ./kube/postgres-configmap.yml
	kubectl apply -f ./kube/postgres-secrets.yml
	kubectl apply -f ./kube/postgres-deployment.yml
	kubectl apply -f ./kube/postgres-service.yml
	kubectl apply -f ./kube/pgadmin-secrets.yml
	kubectl apply -f ./kube/pgadmin-deployment.yml
	kubectl apply -f ./kube/pgadmin-service.yml
	kubectl apply -f ./kube/server-deployment.yml
	kubectl apply -f ./kube/server-service.yml
	kubectl apply -f ./kube/client-deployment.yml
	kubectl apply -f ./kube/client-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets postgres-secrets
	kubectl delete configMap postgres-configmap
	kubectl delete persistentvolumeclaim postgres-pvc
	kubectl delete secrets pgadmin-secrets
	kubectl delete ingress pgadmin

kube-port-forward-server:
	kubectl port-forward service/securityoauth2server 8080:8080
kube-port-forward-client:
	kubectl port-forward service/securityoauth2client 8081:8081

kube-port-forward-postgres:
	kubectl port-forward service/postgres 5432:5432

kube-port-forward-pgadmin:
	kubectl port-forward service/pgadmin 9090:80
