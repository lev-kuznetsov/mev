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
kubectl exec -it $(kubectl get pod --selector app=rserve --output jsonpath={.items[*].metadata.name}) -- R --no-save
```

Shaping a running container does not change the underlying image, any changes made will be destroyed with the container.  A new image is required to save changes. The following will create a template for the docker file:

```
print "FROM $(kubectl get pod --selector app=rserve --output jsonpath='{.items[0].spec.containers[?(@.name == "rserve")].image}')\n\nRUN R --no-save -e \"source('http://bioconductor.org/biocLite.R');install.packages(c());biocLite(c())\" && rm -rf /tmp/*\n"
```
Build and tag the image
```
docker build -t docker.io/cccb/mev-rserve:`date +"%Y-%m-%d"` .
```

Change the image worker deployment definition in src/main/kubernetes/rserve/rserve-worker.yaml, after everything is satisfactory don't forget to push the image up to dockerhub