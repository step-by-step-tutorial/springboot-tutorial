kubectl delete all --all
kubectl delete secrets postgres-secrets
kubectl delete configMap postgres-configmap
kubectl delete persistentvolumeclaim postgres-pvc
kubectl delete secrets pgadmin-secrets
kubectl delete ingress pgadmin

kubectl apply -f ./kube/postgres-pvc.yml

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

kubectl apply -f ./kube/pgadmin-secrets.yml
REM kubectl describe secret pgadmin-secrets -n default
REM kubectl get secret pgadmin-secrets -n default -o yaml

kubectl apply -f ./kube/pgadmin-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment pgadmin -n default

kubectl apply -f ./kube/pgadmin-service.yml
REM kubectl get services -n default
REM kubectl describe service pgadmin -n default

REM kubectl apply -f ./kube/pgadmin-ingress.yml
REM kubectl get ingress -n default
REM kubectl describe ingress pgadmin -n default
kubectl get all

kubectl port-forward service/pgadmin 8080:80
kubectl port-forward service/postgres 5432:5432