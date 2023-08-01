# delete pods,services, deployments, etc
kubectl delete all --all

# delete mysql stuff
kubectl delete secrets mysql-secrets
kubectl delete configMap mysql-configmap
kubectl delete persistentvolumeclaim mysql-pvc

# delete adminer stuff
kubectl delete secrets adminer-secrets