[![Build Status](https://travis-ci.org/lev-kuznetsov/mev.svg?branch=crystal)](https://travis-ci.org/lev-kuznetsov/mev)
===

MultiExperiment Viewer

MeV is a microarray and sequencing data management and analysis application. The system provides a variety of clustering and statistical analyses facilitating in creating a meaningful view of the expression data

===

Building requires JDK 7, Maven 3, R 3.1.2 with the toolkit for building R packages from source and the raven R package. Checkout or download and unzip the source, fire up a terminal and build:
```
mvn clean install && echo "raven::raven()" | R
```
To launch move over to the webapp folder, start the application with:
```
cd webapp
R=/usr/bin/R mvn rserve:start jetty:run rserve:stop
```
and point your browser to http://localhost:8080
