[MultiExperiment Viewer](http://mev.tm4.org) [![Build Status](https://travis-ci.org/dfci-cccb/mev.svg?branch=master)](https://travis-ci.org/dfci-cccb/mev)
<!-- The following thoughts should really be saved as wiki pages -->
===

To run locally install [kubectl](https://kubernetes.io/docs/user-guide/prereqs/) and [minikube](https://kubernetes.io/docs/getting-started-guides/minikube/#installation) then follow minikube instructions to start up a cluster. Pull the source, feed the cluster definition to kubernetes and open the web application:
```
git clone https://github.com/dfci-cccb/mev
kubectl create -f src/main/kubernetes
minikube service mev
```
The last instruction will eventually open the web application in a new default browser window (the first time may take a while as all the container images need to be pulled down)

Provisioned cluster does not retain data, all content will be destroyed with the cluster. To tear down all services run
```
kubectl delete -f src/main/kubernetes
```

===

Hacking is a bit more involved the development workflow is to forego creating a new docker image for every change in the application. The application expects to run inside kubernetes environment, system environment must be set up correctly to enable it to run outside kubernetes

This will set up the environment variables pointing to the Rserve pod
```
export RSERVE_SERVICE_HOST="$(minikube service rserve --url | awk -F":" '{print $2}' | sed 's/\///g')"
export RSERVE_SERVICE_PORT="$(minikube service rserve --url | awk -F":" '{print $3}')"
```

This will set up the environment variables pointing to the ElasticSearch cluster
```
export ELASTICSEARCH_SERVICE_HOST="$(minikube service elasticsearch --url | awk -F":" '{print $2}' | sed 's/\///g')"
export ES_DATA_SERVICE_PORT_TRANSPORT="$(minikube service elastic search --url | awk -F":" '{print $3}')"
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

===

All kubernetes definitions included in the source are aimed at development; in production internal services (anything other than the web application itself) should be changed from type 'NodePort' to 'ClusterIP'. The web application service should be exposed and the type set to 'LoadBalancer'

Rserve deployment definition calls for a single replica, which is to ease development, obviously the number of replicas should be at least 2 or higher to ensure a running worker up at all times. The number of simultaneously executing analyses **per running web application container** is governed by the number of threads in the ExecutorService bean as defined in edu.cccb.mev.context.Concurrency (number of available processors as of this writing.) Deployment elasticity should be handled through ```kubectl autoscale``` directives. Autoscaling the cluster should be handled by the cloud provider, not the application

Data retention in cluster environment is a complex problem so any data retention services such as database and ElasticSearch should ideally not be a part of kubernetes cluster and instead be provisioned by the cloud provider and the web application containers linked to these services
