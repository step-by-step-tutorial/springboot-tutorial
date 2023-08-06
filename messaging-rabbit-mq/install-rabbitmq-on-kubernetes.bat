REM ====================================================================================================================
REM Rabbitmq
REM ====================================================================================================================
kubectl apply -f ./kube/rabbitmq-secrets.yml
REM kubectl describe secret rabbitmq-secrets -n default
REM kubectl get secret rabbitmq-secrets -n default -o yaml

kubectl apply -f ./kube/rabbitmq-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment rabbitmq -n default

kubectl apply -f ./kube/rabbitmq-service.yml
REM kubectl get service -n default
REM kubectl describe service rabbitmq -n default

REM ====================================================================================================================
REM After Install
REM ====================================================================================================================
kubectl get all

REM ====================================================================================================================
REM Access from localhost
REM ====================================================================================================================

REM if you want to connect to rabbitmq from localhost through the web browser or application use the following command
REM http://localhost:15672
kubectl port-forward service/rabbitmq 15672:15672
