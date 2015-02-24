library(NMF)
# Nonnegative Factorization Matrix
# Required R 3.0 or above
#
#
# to install the package from NMF source
## source('http://bioconductor.org/biocLite.R')
# or
## library(BiocInstaller)
# Example: install NMF package
# On Windows or Mac OS X, this will try install the source package 
# if a binary package is not available for the OS version
# scales     0.2.3  0.2.4             FALSE
# plyr         1.8  1.8.1              TRUE
# ggplot2  0.9.3.1  1.0.0             FALSE
# reshape2   1.2.2  1.4.1             FALSE

## biocLite("Rcpp")
## biocLite("plyr")
## biocLite('ggplot2')
## biocLite('reshape2')
## biocLite("scales")
## biocLite("pkgmaker")
## biocLite("rngtools")


#
# REQUIRED INPUTS
# in.mtx 	Data matrix 	
# rank		the factorization rank (i.e the number of columns in matrix W, the number of target group)
# method	the algorithm to estimate the factorization.  Options are:
#			brunet, lee, offset, nsNMF
# seed		the seeding method used to compute the starting point.  Options are:
#			ica, nndsvd, none, random
#


##
##
## read files
#in.mtx=read.table(INFILE, header=TRUE, sep="\t", nrow=-1)

#in.mtx=read.table("../../../../data/Affymetrix/affy_U133plus2_dataset.txt", header=TRUE, sep="\t", nrow=-1)
in.mtx=read.table(INFILE, header=TRUE, sep="\t", nrow=-1)

##
## Assume the file starts with \t and the first column is gene id
rownames(in.mtx)=in.mtx[,1]
in.mtx=na.omit(in.mtx[,-1])
in.mtx=data.matrix(in.mtx)


data(esGolub)
in.mtx=esGolub
res<-nmf(in.mtx, 3, method ="brunet", seed="nndsvd", nrun=10, options=list(keep.all=TRUE))


V.hat=fitted(res)










