REM ====================================================================================================================
REM PostgreSQL
REM ====================================================================================================================
kubectl apply -f ./kube/postgres-pvc.yml
REM kubectl get pvc
REM kubectl describe pvc postgres-pvc

kubectl apply -f ./kube/postgres-configmap.yml
REM kubectl describe configmap postgres-configmap -n default

kubectl apply -f ./kube/postgres-secrets.yml
REM kubectl describe secret postgres-secrets -n default
REM kubectl get secret postgres-secrets -n default -o yaml

kubectl apply -f ./kube/postgres-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment postgres -n default

kubectl apply -f ./kube/postgres-service.yml
REM kubectl get service -n default
REM kubectl describe service postgres -n default

REM ====================================================================================================================
REM Pgadmin
REM ====================================================================================================================

kubectl apply -f ./kube/pgadmin-secrets.yml
REM kubectl describe secret pgadmin-secrets -n default
REM kubectl get secret pgadmin-secrets -n default -o yaml

kubectl apply -f ./kube/pgadmin-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment pgadmin -n default

kubectl apply -f ./kube/pgadmin-service.yml
REM kubectl get services -n default
REM kubectl describe service pgadmin -n default

REM ====================================================================================================================
REM Adminer
REM ====================================================================================================================

REM kubectl apply -f ./kube/adminer-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment adminer -n default

REM kubectl apply -f ./kube/adminer-service.yml
REM kubectl get services -n default
REM kubectl describe service adminer -n default

REM ====================================================================================================================
REM After Install
REM ====================================================================================================================
kubectl get all

REM ====================================================================================================================
REM Access from localhost
REM ====================================================================================================================
REM if you want to connect database from localhost through the application use the following command
kubectl port-forward service/postgres 5432:5432

REM if you want to connect to pgadmin from localhost through the web browser use the following command
REM http://localhost:8080
kubectl port-forward service/pgadmin 8080:80

REM if you want to connect to adminer from localhost through the web browser use the following command
REM http://localhost:8080
REM kubectl port-forward service/adminer 8080:8080
