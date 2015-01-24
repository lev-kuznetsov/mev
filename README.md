[![Build Status](https://travis-ci.org/lev-kuznetsov/mev.svg?branch=crystal)](https://travis-ci.org/lev-kuznetsov/mev)
===

MultiExperiment Viewer

MeV is a microarray and sequencing data management and analysis application. The system provides a variety of clustering and statistical analyses facilitating in creating a meaningful view of the expression data

===

Application server build requires JDK7 and Maven 3. This terminal directive launched from the cloned source root directory will build and test the application server
```
mvn clean install
```
R utilities build requires R version 3.1.2 and with the raven package installed, the following directive will install the raven package from the R prompt
```
install.packages ('devtools');
devtools::install_github ('dfci-cccb/raven');
```
After the raven package has been installed launch raven at R prompt from the cloned source root directory to build and test the R utilities
```
raven::raven ();
```
Web client build requires npm with gulp and bower installed globally. The following directive at the terminal prompt from the cloned source directory will build and test the web client
```
gulp build test
```
After completing all of the previous steps the application is ready to launch; move over to the webapp folder and start the application
```
cd webapp
mvn rserve:start jetty:run rserve:stop
```
You may now point your browser to http://localhost:8080

You can specify path to R executable with the "R" environment variable