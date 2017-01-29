[MultiExperiment Viewer](http://mev.tm4.org) [![Build Status](https://travis-ci.org/dfci-cccb/mev.svg?branch=master)](https://travis-ci.org/dfci-cccb/mev)

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
