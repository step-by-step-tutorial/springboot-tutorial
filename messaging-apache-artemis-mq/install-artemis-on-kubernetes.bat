REM ====================================================================================================================
REM Artemis
REM ====================================================================================================================
kubectl apply -f ./kube/artemis-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment artemis -n default

kubectl apply -f ./kube/artemis-service.yml
REM kubectl get service -n default
REM kubectl describe service artemis -n default

REM ====================================================================================================================
REM After Install
REM ====================================================================================================================
kubectl get all

REM ====================================================================================================================
REM Access from localhost
REM ====================================================================================================================
REM if you want to connect to artemis from localhost through the web browser or application use the following command
REM http://localhost:8161
kubectl port-forward service/artemis 8161:8161