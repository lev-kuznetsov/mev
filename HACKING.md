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

Then you can run the web application via `mvn wildfly:run` and point your browser to `localhost:8080`

The REST api root resource is located at `/p` where you will find your workspace represented by a list of your projects by name. A project consistent of items, an item may be a dataset or an analysis. The type of the item is denoted by the `@c` JSON property. From there subresources will differ based on the type of parent resource. For example datasets can be queried for values and analyses types will have endpoints for parameters and results. Compound resources may be posted in one request with json or each piece can be filled in separately. Content negotiation is via `Content-Type` and `Accept` headers, both of which must be set. Cookies are required currently as they store the workspace identifier, here's an example `PUT` request body for a workspace to `/p` with `Content-Type: application/json`, `Accept: application/json` :

```
{
  "bar": {
    "baz": {
      "@c": "edu.dfci.cccb.mev.analysis.r.example.Example",
      "a": 3, "b": 5, "state": "STARTING"
    },
    "foo": {
      "@c": "edu.dfci.cccb.mev.dataset.literal.Literal",
      "values": {
        "r1": {"c1":0.1,"c2":0.2},
        "r2": {"c1":0.2,"c2":0.3},
        "r3": {"c1":0.3,"c2":0.4},
        "r4": {"c1":0.4,"c2":0.5}
      }
    }
  }
}
```
This will set the `bar` project in your workspace with a literal dataset named `foo` (datasets can also be referenced or loaded from a preset) and analysis `baz` of type `edu.dfci.cccb.mev.analysis.r.example.Example` with parameters `a` and `b`. `state:STARTING` is a magic symbol to trigger the analysis start. The `@c` property corresponds to fully qualified class name of the analysis type where in this particular case you will find a rather simple resource with fields `a`, `b`, and `c`. Annotations attach REST (via JAX-RS), serialization (via Jackson), persistence (via JPA) and R context behaviors. `edu.dfci.cccb.mev.analysis.r.Rserve` handles the R context mapping on a persistence callback

From here you can query your project with a `GET` request to `/p/bar` which will look like this:
```
{
  "foo": {
    "@c": "edu.dfci.cccb.mev.dataset.literal.Literal"
  },
  "baz": {
    "@c": "edu.dfci.cccb.mev.analysis.r.example.Example",
    "state": "COMPLETED"
  }
}
```
And you can retrieve your result stored in the `c` field via `/p/bar/baz/c`. This is a simple analysis and unless you're super quick about fetching analysis state it'll be completed, but you may see `RUNNING` there instead and the result field will not be filled in. In case an error happens, an `error` field will be present with a string message of what may have gone wrong

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