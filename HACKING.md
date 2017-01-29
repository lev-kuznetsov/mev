Hacking is a bit more involved if development cycle is to forego creating a new docker image for every change in the application. The application expects to run inside kubernetes environment, system environment must be set up accordingly to enable it to run outside kubernetes

This will set up the environment variables pointing to the Rserve pod
```
export RSERVE_SERVICE_HOST="$(minikube service rserve --url | awk -F":" '{print $2}' | sed 's/\///g')"
export RSERVE_SERVICE_PORT="$(minikube service rserve --url | awk -F":" '{print $3}')"
```

This will set up the environment variables pointing to the ElasticSearch cluster
```
export ELASTICSEARCH_SERVICE_HOST="$(minikube service elasticsearch --url | awk -F":" '{print $2}' | sed 's/\///g')"
export ELASTICSEARCH_SERVICE_PORT="$(minikube service elasticsearch --url | awk -F":" '{print $3}')"
```

Then you can run the web application via ```mvn wildfly:run``` and point your browser to ```localhost:8080```

During development of a new analysis type it may be beneficial to install R or system packages, it's possible to open an R shell to the pod
```
kubectl exec -it $(kubectl get pod --selector component=rserve --output jsonpath={.items[*].metadata.name}) -- R --no-save
```

It's also possible to follow the Rserve log in the same way
```
kubectl exec -it $(kubectl get pod --selector component=rserve --output jsonpath={.items[*].metadata.name}) -- tail -f /var/log/rserve.log
```

Shaping a running container does not change the underlying image, any changes made will be destroyed with the container.  A new image is required to save changes. Edit the docker file in src/main/docker/rserve basing on the previous image, build and tag the image
```
mvn docker:build@rserve
```

Change the image worker deployment definition in src/main/kubernetes/rserve/rserve-worker.yaml, after everything is satisfactory don't forget to push the image up to dockerhub