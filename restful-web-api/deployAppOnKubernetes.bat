REM ======================================================================================================================
REM RESTFUL-WEB-API
REM ======================================================================================================================
docker build -t samanalishiri/restful-web-services:latest .

kubectl apply -f .\kube\app-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment restfulwebapi -n default

kubectl apply -f .\kube\app-service.yml
REM kubectl get service -n default
REM kubectl describe service restfulwebapi -n default

REM ======================================================================================================================
REM After Install
REM ======================================================================================================================
kubectl get all

REM ======================================================================================================================
REM Access from localhost
REM ======================================================================================================================

REM if you want to connect to Application from localhost through the web browser use the following command
REM http://localhost:8080
kubectl port-forward service/restfulwebapi 8080:8080