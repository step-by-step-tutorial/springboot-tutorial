# ======================================================================================================================
# RESTFUL-WEB-API
# ======================================================================================================================
docker build -t samanalishiri/restfulwebapi:latest .

kubectl apply -f ./kube/app-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment restfulwebapi -n default

kubectl apply -f ./kube/app-service.yml
# kubectl get service -n default
# kubectl describe service restfulwebapi -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all

# ======================================================================================================================
# Access from localhost
# ======================================================================================================================

# if you want to connect to Application from localhost through the web browser use the following command
# http://localhost:8080
kubectl port-forward service/restfulwebapi 8080:8080