kubectl delete all --all

kubectl delete secrets postgres-secrets
kubectl delete configMap postgres-configmap
kubectl delete persistentvolumeclaim postgres-pvc

kubectl delete secrets pgadmin-secrets
kubectl delete ingress pgadmin