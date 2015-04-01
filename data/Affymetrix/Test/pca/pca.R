## Principle Component Analysis

## input variables from MeV:
# "../../../../data/Affymetrix/affy_U133plus2_dataset.txt": DATA FILE matrix
# SAMPLE_FILE: File store sample groups
# 
## output file
# "../../../../data/Affymetrix/Test/pca/"
# "../../../../data/Affymetrix/Test/pca/"/pca_result.txt
# "../../../../data/Affymetrix/Test/pca/"/pca_sdev.txt
#



## read files
in.mtx=read.table("../../../../data/Affymetrix/affy_U133plus2_dataset.txt", header=TRUE, sep="\t", nrow=-1)

##
## Assume the file starts with \t and the first column is gene id
rownames(in.mtx)=in.mtx[,1]
in.mtx=na.omit(in.mtx[,-1])
in.mtx=data.matrix(in.mtx)

pca_result=prcomp(t(in.mtx))

write.table(file=paste("../../../../data/Affymetrix/Test/pca/", "pca_result.txt", sep=""), pca_result$x, sep='\t',col.names=TRUE, row.names=TRUE, quote=FALSE)
write.table(file=paste("../../../../data/Affymetrix/Test/pca/", "pca_sdev.txt", sep=""), pca_result$sdev, sep='\t',col.names=TRUE, row.names=TRUE, quote=FALSE)
