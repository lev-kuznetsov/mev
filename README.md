[![Build Status](https://travis-ci.org/lev-kuznetsov/mev.svg?branch=crystal)](https://travis-ci.org/lev-kuznetsov/mev) [![Coverage Status](https://coveralls.io/repos/lev-kuznetsov/mev/badge.png?branch=crystal)](https://coveralls.io/r/lev-kuznetsov/mev?branch=crystal)
===

MultiExperiment Viewer

MeV is a microarray and sequencing data management and analysis application. The system provides a variety of clustering and statistical analyses facilitating in creating a meaningful view of the expression data

===

Building requires JDK 7 and Maven 3. Checkout or download and unzip the source, fire up a terminal and build:
```
mvn clean install
```
After that's done pop over to the webapp folder, start the application with:
```
mvn jetty:run
```
and point your browser to http://localhost:8080