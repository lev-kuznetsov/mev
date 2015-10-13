shim ('limma', callback=function (eBayes, lmFit, topTable) {
  define (voom = function (dataset, control, experiment) {
  
##library(limma)  # imported by shim()
##library(DESeq)

## NOTE
## Performs limma on a *RNASeq count data matrix 
## Input two files: 1) data matrix; 2) Sample definition matrix
## Sample definition matrix contains sample number and status (tab delim)
## Status 1 = Experiment
## Status 0 = Control
## Status -1= Not included in analysis
##
## Data Matrix Assumption
## 
## 1) No "\'" in the input data file
## 2) Count data ONLY
##
## DATE: 7.24.2015
## Author: Yaoyu
##
##
##
## Note
## Adjust input matrix to handle negative values in matrix
## Add in readjustment for Average Expression column 
## Require same output as LIMMA
##


## definte limma function
run.voom<-function(in.mtx, Experiment=NA, Control=NA){
    
  CurrMtx=in.mtx    			# Assume Count Matrix
  CurrMtx=in.mtx[,Experiment]
  CurrMtx=cbind(CurrMtx, in.mtx[,Control])
  
  Tissue=array(NA, dim=c(1,length(colnames(CurrMtx))))
  Tissue[1:length(Experiment)]="Experiment"
  Tissue[(length(Experiment)+1):length(colnames(CurrMtx))]="Control"
  
  Tissue=factor(Tissue,levels=c("Experiment","Control"))
  design=model.matrix(~0+Tissue)
  colnames(design)=c("Experiment", "Control")
  
  dge<-DGEList(counts=CurrMtx)
  dge<-calcNormFactors(dge)         # calculate Normalization factor
  
  # Transform count data to log2-counts per million (logCPM), estimate the mean-variance relationship and use this to 
    #  compute appropriate observational-level weights. The data are then ready for linear modelling.
  v<-voom(dge, design, plot=FALSE)	# Transform 

  # Perform Linear model on transformed data	  
  fit<-lmFit(v, design)
  efit<-eBayes(fit)
  
  contrast.matrix=makeContrasts(ExpvsContr=Experiment-Control, levels=design)
  fit<-eBayes(contrasts.fit(fit, contrast.matrix[,"ExpvsContr"]))
  result_full=topTable(fit, adjust="fdr", number=dim(CurrMtx)[1])
  return(result_full)
}   


##
##
## read files
in.mtx=read.table(INFILE, header=TRUE, sep="\t", nrow=-1)

##
## Assume the file starts with \t and the first column is gene id
rownames(in.mtx)=in.mtx[,1]
in.mtx=na.omit(in.mtx[,-1])
in.mtx=data.matrix(in.mtx)

## Check to determine if matrix contains negative values
## off set the matrix to operate in positive values for limma
## starting with 0
min.val=min(in.mtx)
in.mtx=if(min.val<0){in.mtx+min.val*-1}else{in.mtx}


## Assign group
sample.mtx=read.table(SAMPLE_FILE, header=FALSE, sep="\t")
EXP=sample.mtx[(sample.mtx[,2]==1),1]
CON=sample.mtx[(sample.mtx[,2]==0),1]

##
##  Run limma
result=run.voom(in.mtx,
  Experiment=EXP,
  Control=CON)

##
## reassign colnames
#colnames(result)=c("ID", "Log Fold Change", "Average Expression", "t", "P-value", "q-value", "B")
colnames(result)=c("ID", "lfc", "ae", "t", "pValue", "qValue", "B")

## If the matrix contains negative values,  
## adjust the offset back to its original input values
##
result[,"Average Expression"]=if(min.val<0){result[,"Average Expression"]-(min.val*-1)}else{result[,"Average Expression"]}

##
## write rnk file
gsea_rnk <-result[,c("ID", "t")]
  
  
  
  
  	# result in form of:
#  	list (list (id = <geneId>, logFoldChange = <lfc>, averageExpression = <ae>, pValue = <pv>, qValue = <qv>));
	geneID=result[,"ID"]
	lfc=result[,"lfc"]
	ae=result[,"ae"]
	pv=result[,"pValue"]
	qv=result[,"qValue"]


  	list (list (id = geneId, logFoldChange = lfc, averageExpression = ae, pValue = pv, qValue = qv));
  });
}, binder = binder ());
