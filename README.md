MultiExperiment Viewer

===

[![Join the chat at https://gitter.im/dfci-cccb/mev](https://badges.gitter.im/dfci-cccb/mev.svg)](https://gitter.im/dfci-cccb/mev?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

The new web client MeV
 
[![Build Status](https://travis-ci.org/dfci-cccb/mev.svg?branch=master)](https://travis-ci.org/dfci-cccb/mev)

===

To build you need to have Java 7, maven 3.0+ and R 3.0+

Check out source and from the target folder run the build command from the console:

```mvn clean install```

Then change to console directory to the web folder to install R packages and start the server:

```mvn rserve:start jetty:run rserve:stop```

This assumes ```R``` command is on the path, you may specify path via an environment variable like this:

```R=/path/to/R mvn rserve:start jetty:run rserve:stop```

You may now point your browser to ```http://localhost:8080```
