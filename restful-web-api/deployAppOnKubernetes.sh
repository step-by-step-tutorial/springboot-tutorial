# ======================================================================================================================
# RESTFUL-WEB-API
# ======================================================================================================================
docker build -t samanalishiri/restful-web-services:latest .

kubectl apply -f ./kube/app-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment app -n default

kubectl apply -f ./kube/app-service.yml
# kubectl get service -n default
# kubectl describe service app -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all

# ======================================================================================================================
# Access from localhost
# ======================================================================================================================

# if you want to connect to adminer from localhost through the web browser use the following command
# http://localhost:8080
kubectl port-forward service/restful-web-api 8080:8080