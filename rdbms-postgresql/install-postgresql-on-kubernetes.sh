# ======================================================================================================================
# PostgreSQL
# ======================================================================================================================
kubectl apply -f ./kube/postgres-pvc.yml
# kubectl get pvc 
# kubectl describe pvc postgres-pvc

kubectl apply -f ./kube/postgres-configmap.yml
# kubectl describe configmap postgres-configmap -n default

kubectl apply -f ./kube/postgres-secrets.yml
# kubectl describe secret postgres-secrets -n default
# kubectl get secret postgres-secrets -n default -o yaml

kubectl apply -f ./kube/postgres-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment postgres -n default

kubectl apply -f ./kube/postgres-service.yml
# kubectl get service -n default
# kubectl describe service postgres -n default

# ======================================================================================================================
# Pgadmin
# ======================================================================================================================
kubectl apply -f ./kube/pgadmin-secrets.yml
# kubectl describe secret pgadmin-secrets -n default
# kubectl get secret pgadmin-secrets -n default -o yaml

kubectl apply -f ./kube/pgadmin-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment pgadmin -n default

kubectl apply -f ./kube/pgadmin-service.yml
# kubectl get services -n default
# kubectl describe service pgadmin -n default

# ======================================================================================================================
# Adminer
# ======================================================================================================================
# kubectl apply -f ./kube/adminer-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment adminer -n default

# kubectl apply -f ./kube/adminer-service.yml
# kubectl get services -n default
# kubectl describe service adminer -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all

# ======================================================================================================================
# Access from localhost
# ======================================================================================================================
# if you want to connect database from localhost through the application use the following command
kubectl port-forward service/postgres 5432:5432

# if you want to connect to pgadmin from localhost through the web browser use the following command
# http://localhost:8080
kubectl port-forward service/pgadmin 8080:80

# if you want to connect to adminer from localhost through the web browser use the following command
# http://localhost:8080
# kubectl port-forward service/adminer 8080:8080
