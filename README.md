MultiExperiment Viewer

===

The new web client MeV
 
[![Build Status](https://travis-ci.org/dfci-cccb/mev.png?branch=master)](https://travis-ci.org/dfci-cccb/mev)

===

To build you need to have Java 7, maven 3.0+ and R 2.15 with limma and multtest packages installed from bioconductor. Once you have all of that, open a command line window at the source folder, type "mvn clean install" once that's done change folder to web and launch the application server: "cd web/; mvn jetty:run" After that point your browser to http://localhost:8080
