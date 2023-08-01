# delete pods,services, deployments, etc
kubectl delete all --all

# delete postgres stuff
kubectl delete secrets postgres-secrets
kubectl delete configMap postgres-configmap
kubectl delete persistentvolumeclaim postgres-pvc

# delete pgadmin stuff
kubectl delete secrets pgadmin-secrets
kubectl delete ingress pgadmin