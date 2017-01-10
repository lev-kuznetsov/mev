Setting up development environment

Running the application server outside a kubernetes pod requires setting the following environment variables

```
export MEV_RSERVE_SERVICE_HOST="$(kubectl get pod --selector app=mev-rserve --output jsonpath={.items[*].status.hostIP})"
export MEV_RSERVE_SERVICE_PORT="$(kubectl get service mev-rserve --output jsonpath={.spec.ports[*].nodePort})"
export ELASTICSEARCH_SERVICE_HOST="$(kubectl get pod --selector component=elasticsearch --output jsonpath={.items[*].status.hostIP})"
export ELASTICSEARCH_SERVICE_PORT_HTTP=$(kubectl get service elasticsearch --output jsonpath='{.spec.ports[?(@.name == "http")].nodePort}')
export ELASTICSEARCH_SERVICE_PORT_TRANSPORT=$(kubectl get service elasticsearch --output jsonpath='{.spec.ports[?(@.name == "transport")].nodePort}')
```

It's possible to open an R session into the Rserve pod via

```kubectl exec -it $(kubectl get pod --selector app=mev-rserve --output jsonpath={.items[*].metadata.name}) -- R --no-save```

At which point it's possible to shape the container including installation of packages
