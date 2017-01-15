# The MIT License (MIT)
# Copyright (c) 2017 Dana-Farber Cancer Institute
#
# Permission is hereby granted, free of charge, to any person
# obtaining a copy of this software and associated documentation
# files (the "Software"), to deal in the Software without
# restriction, including without limitation the rights to use,
# copy, modify, merge, publish, distribute, sublicense, and/or
# sell copies of the Software, and to permit persons to whom the
# Software is furnished to do so, subject to the following
# conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
# OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
# OTHER DEALINGS IN THE SOFTWARE.


# Normalization
# 
# Author: levk

dataset <- as.data.frame (dataset);
phenoData=NULL;featureData=NULL;log = TRUE;
library('rafalib')
library('metagenomeSeq')
library('DESeq')
library('edgeR')
library('DESeq2')
library('matrixStats')
counts <- data.matrix (dataset)
obj = newMRexperiment(dataset,phenoData,featureData)
obj = cumNorm(obj)
METHODS <- c('css','css2','tss', 'deseq', 'tmm','upperquantile','vst','pa')
method <- pmatch(tolower(method), METHODS)
if (is.na(method)) stop('invalid normalization method')
if (method == -1) stop('ambiguous transformation method')
if(method == '1') nmat = MRcounts(obj,log=FALSE,norm=TRUE,sl=median(normFactors(obj)))
counts = MRcounts(obj,norm=FALSE,log=FALSE)
normNewCSS <-function(obj,p){
	normFactor = unlist(metagenomeSeq::calcNormFactors(obj, p = p))
	sweep(obj,2,normFactor/median(normFactor),'/')
}
if(method == '2'){
	css2Normalization<-function(mat,rel=.1){
		smat = sapply(1:ncol(mat),function(i){sort(mat[,i],decreasing=FALSE)})
		ref  = rowMeans(smat)
		yy = mat
		yy[yy==0]=NA
		ncols = ncol(mat)
		refS = sort(ref)
		k = which(refS>0)[1]
		lo = (length(refS)-k+1)
		diffr = sapply(1:ncols,function(i){
					refS[k:length(refS)] - quantile(yy[,i],p=seq(0,1,length.out=lo),na.rm=TRUE)
				})
		q = sapply(1:ncol(diffr),function(i){
					col = diffr[,i]
					which(abs(diff(col))/col[-1]>rel)[1] / length(col)
				})
		q[is.na(q)] = median(q,na.rm=TRUE)
		names(q) = colnames(mat)
		diffr2 = matrixStats::rowMedians(abs(diffr),na.rm=TRUE)
		x = which(abs(diff(diffr2))/diffr2[-1]>rel)[1] / length(diffr2)
		if(x<=0.50) x = 0.50
		typicalCSS = x
		newCSS = q
		unlist(newCSS)
	}
	p = css2Normalization(counts)
	nmat = normNewCSS(counts,p)
}
if(method == '3'){
	tss = colSums(counts)
	nmat = sweep(counts,2,tss,'/')
}
if(method == '4'){
	deseq_sf = estimateSizeFactorsForMatrix(counts)
	nmat = sweep(counts,2,deseq_sf,'/')
}
if(method == '5'){
	d <- DGEList(counts, lib.size = as.vector(colSums(counts)))
	d <- calcNormFactors(d,method='TMM')
	nmat = cpm(d, normalized.lib.size=TRUE)
}
if(method == '6'){
	y = counts
	y[y==0] = NA
	scale = 1
	normalizationScale = colQuantiles(y,p=.75,na.rm=TRUE)
	nmat = sweep(counts,2,normalizationScale/scale,'/')
}
if(log && exists('nmat')) nmat = log2(nmat+1)
if(method == '7'){
	dds <- DESeqDataSetFromMatrix(counts, pData(obj), ~1)
	dds <- estimateSizeFactors(dds)
	dds <- estimateDispersions(dds)
	nmat = getVarianceStabilizedData(dds)
}
if(method == '8') nmat = 1-(1-counts>0);
normalized <- as.list (nd);
