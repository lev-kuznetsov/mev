#MultiExperiment Viewer

===

[![Join the chat at https://gitter.im/dfci-cccb/mev](https://badges.gitter.im/dfci-cccb/mev.svg)](https://gitter.im/dfci-cccb/mev?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

The new web client MeV
 
[![Build Status](https://travis-ci.org/dfci-cccb/mev.svg?branch=master)](https://travis-ci.org/dfci-cccb/mev)

===

To build you need to have Java 7, maven 3.0+, R 3.0+, node 4.5.0 and npm 2.15+
The following instructions pertain to Ubuntu 16.04, OSX install procedure will slightly differ  

##Development
###Get Source
Fork the repository then download source
```
mkdir -p ~/git/mev
cd ~/git/mev
git clone https://github.com/yourusername/mev.git
git remote add upstream https://github.com/dfci-cccb/mev.git 
```
###Install Dependencies
####ProtoBuff
```
sudo apt-get install build-essential libtool
sudo apt-get install autoconf
cd ~/git
git clone https://github.com/dfci-cccb/protobuf.git
cd protobuf
./autogen.sh
./configure --prefix=/usr
make
make check
sudo make install
```

#####Install R
*Dependencies*
```
sudo apt-get install gfortran
sudo apt-get install libreadline-dev
```
Install R from http://lib.stat.cmu.edu/R/CRAN/ 


######Install R Packages
Install from R console
#######Multtest
```
> source("https://bioconductor.org/biocLite.R")
biocLite("multtest")
```
#######DESeq
*Dependencies*```sudo apt install libxml2-dev```
```
source("http://bioconductor.org/biocLite.R");
biocLite("DESeq");
```
######RProtoBuff
*Dependencies*:
```sudo apt install libcurl4-gnutls-dev```

###Build Web App
```
cd ~/git/mev
mvn clean install
```
###Web Client
*Assumes node 4.5.0 and npm version 2.15+ is installed is intalled*
####Install Node Packages
*Dependency:*
(Required: Required by Vegajs for mevNetworkGraph)
```
sudo apt-get install libcairo2-dev libjpeg-dev libgif-dev
```
```
npm install -g linklocal
npm install -g bower
npm install -g gulp grunt
```

####Build Web Client
```
cd ~/git/mev/web/src/main/javascript/edu/dfci/cccb/mev/web/ui
linklocal -r
bower install
npm run dev
gulp
```
###Run
After installing the dependencies and building web app and web client you're ready to run. First launch RServe, then the WebApp 
####Start RServe
```
cd ~/git/mev/web
mvn rserve:run
```
*Note: when running for the first time the above command will install required R packages*

####Start Web Server
```
cd ~/git/mev/web
mvn jetty:run
```
You may now point your browser to http://localhost:8080
