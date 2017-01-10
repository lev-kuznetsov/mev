
# Useful commands:
#  - Start dev profile for all services in kubernetes
#   python mevctl.py c d | kubectl create -
#  - Start dev profile rserve service in kubernetes
#   python mevctl.py c d r | kubectl create -
#  - Autoconfigure environment for minikube launched rserve
#   $eval (minikube service list -n default --format "{{.IP}}:{{.Port}}" | python mevctl.py e r)

import sys, yaml;

directive = sys.argv[1] if len (sys.argv) > 1 else "";

rserveImage = 'cccb/mev-rserve:2017-01-05';
webImage = 'cccb/mev-web';

# Create
if directive.startswith ('c'):
    items = [];

    profile = sys.argv[2] if len (sys.argv) > 2 else "";
    name = sys.argv[3] if len (sys.argv) > 3 else "all";

    def rc (name, replicas, select, meta, app, container, image, port):
        return { "kind": "ReplicationController",
                 "apiVersion": "v1",
                 "metadata": { "name": name },
                 "spec": { "replicas": replicas,
                           "selector": { "app": select },
                           "template": { "metadata": { "labels": { "app": select }},
                                         "spec": { "containers": [{ "name": container,
                                                                    "image": image, "ports": [{ "containerPort": port }]}]}}}}
    def service (name, app, port, select, type):
        return { 'apiVersion': 'v1',
                 'kind': 'Service',
                 'metadata': { 'name': name },
                 'spec': { 'ports': [{ 'port': port }],
                           'selector': { 'app': select },
                           'type': type }};
    def component (name, replicas, image, port, type):
        return [ rc (name, replicas, name, name, name, name, image, port),
                 service (name, name, port, name, type) ];

    if profile.startswith ("d"):
        rserve = component ('mev-rserve', 1, rserveImage, 6311, 'NodePort');
        web = component ('mev-web', 1, webImage, 80, 'NodePort');
    elif profile.startswith ("p"):
        rserve = component ('mev-rserve', 4, rserveImage, 6311, 'ClusterIP');
        web = component ('mev-web', 2, webImage, 80, 'LoadBalancer');
    else:
        raise Exception ("Unrecognized profile \"{p}\", expecting (d)evelopment or (p)roduction".format (p=profile));

    if name.startswith ('r') or name.startswith ('a'):
        items.extend (rserve)
    if name.startswith ('w') or name.startswith ('a'):
        items.extend (web);

    if len (items) < 1:
        raise Exception ("Unrecognized name \"{c}\", expecting (r)serve or (w)eb".format (c=name));

    print "# Following content should be used as input to kubectl create\n";
    print "\n---\n".join (map (lambda x: yaml.dump (x, default_flow_style=False).rstrip (), items));

# Environment
elif directive.startswith ('e'):
    items = []; rserve = []; web = [];

    name = sys.argv[2] if len (sys.argv) > 2 else "all";

    for line in sys.stdin:
        entry = line.split ('|');
        service = entry[2].strip ();

        def toExport (name, prefix, url):
            if url.find (':') < 0:
                return [ "# {i} for service {s}".format (i=url, s=name) ];
            else:
                host = url[:url.find (':')];
                port = url[url.find (':') + 1:];
                return [ "export {s}_SERVICE_HOST=\"{h}\"".format (s=prefix, h=host),
                         "export {s}_SERVICE_PORT=\"{p}\"".format (s=prefix, p=port) ]
    
        if service == 'mev-rserve':
            rserve = toExport ('mev-rserve', 'MEV_RSERVE', entry[3].strip ());
        if service == 'mev-web':
            web = toExport ('mev-web', 'MEV_WEB', entry[3].strip ());
        
    if name.startswith ('r') or name.startswith ('a'):
        items.extend (rserve);
    if name.startswith ('w') or name.startswith ('a'):
        items.extend (web);
    
    items.append ("# Run this command to configure your shell:")
    items.append ("# $eval (minikube service list -n default --format \"{{.IP}}:{{.Port}}\" | python mevctl.py e [rwa])")
    
    print "\n".join (items);

else:
    raise Exception ("Unrecognized directive \"{d}\", expecting (c)reate or (e)nvironment".format (d=directive));
