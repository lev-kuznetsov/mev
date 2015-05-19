library(topGO)
library (injectoR)
# GENELIST   Gene list of interest	 string vector
# BACKGROUND  Background gene list	 string vector
# SPECIES							 string	
# GO_TYPE							 string
# TEST_TYPE							 string	

library(biomaRt)

# HUMAN GENES
#ensembl=useMart("ensembl")
ensembl = useMart("ensembl",dataset="hsapiens_gene_ensembl")
genes=getBM(c('entrezgene','hgnc_symbol'), mart=ensembl)#   , filters='with_go_id', values='GO:0004707', mart=ensembl)
BACKGROUND=genes[,2] # Human Background



# mouse genes
ensembl=useMart('ensembl', dataset='mmusculus_gene_ensembl')
genes=getBM(c('entrezgene','hgnc_symbol'), mart=ensembl)#   , filters='with_go_id', values='GO:0004707', mart=ensembl)
BACKGROUND=genes[,2] # Human Background


#########



#GO_TYPE="BP"
#TEST_TYPE='Fisher test'
#SPECIES='human'


# GENELIST   Gene list of interest	 string vector
# BACKGROUND  Background gene list	 string vector
# SPECIES							 string	
# GO_TYPE							 string
# TEST_TYPE							 string     options: 'fisher', 'ks_test'
# p.adjust.methods
# c("holm", "hochberg", "hommel", "bonferroni", "BH", "BY",
#   "fdr", "none")
# GO-terms to return


shim('topGO', callback=function (){
	define(gsea = function() function(GENELIST, BACKGROUND, 
	SPECIES='human', 
	GO_TYPE='BP', 
	TEST_TYPE='fisher',
	P_ADJUST='fdr'){
		
	if(SPECIES=='human'){
		library(org.Hs.eg.db)
		anno.db='org.Hs.eg.db'
		ensembl=useMart('ensembl', dataset='hsapiens_gene_ensembl')
	}
	if(SPECIES=='mouse'){
		library(org.Mm.eg.db)
		anno.db='org.Mm.eg.db'
		ensembl=useMart('ensembl', dataset='mmusculus_gene_ensembl')
	}
	if(length(BACKGROUND)==0){
		BACKGROUND=getBM('hgnc_symbol', mart=ensembl)[,1]
	}
	
 	## create a topGOdata object
 	ALL_GENES=unique(c(GENELIST, BACKGROUND))
 	GENE_STATUS=rep(0,length(ALL_GENES))
 	names(GENE_STATUS)=ALL_GENES
 	GENE_STATUS[GENELIST]=1
 	GENE_STATUS=factor(GENE_STATUS)

	#print(GENE_STATUS)

    myGOdata = new('topGOdata', 
 				ontology=GO_TYPE, 
 				description='topGO analysis', 
 				nodeSize=100,
   				annot=annFUN.org, 
   				mapping=anno.db, 
   				ID='symbol', 
   				allGenes=GENE_STATUS)
 	
 	#print(myGOdata)
 
 	## run the enrichment test
 	if (TEST_TYPE=='fisher')
 	    topGO.stat = new('classicCount', testStatistic=GOFisherTest, name=TEST_TYPE)
 	if (TEST_TYPE=='ks_test')
    	 topGO.stat = new('classicScore', testStatistic=GOKSTest, name=TEST_TYPE)

 
    topGO.result = getSigGroups(myGOdata, topGO.stat)
	
	
	#print("get topGO.result")

 	## count the total significant GO groups (nodes)
 	topGO.count = capture.output(topGO.result)
 	totalNodes = as.numeric(unlist(strsplit(topGO.count[5],' '))[1])

 	## summarize the top 100 GO terms and write to the file
 	if (totalNodes >= 100){
 	 	topGO.table = GenTable(myGOdata, topGO.result, topNodes=100)}
 	if (totalNodes < 100){
   		topGO.table = GenTable(myGOdata, topGO.result, topNodes=totalNodes)}
   		
    colnames(topGO.table) = c('GO ID','GO Term','Annotated Genes','Significant Genes','Expected','P-value')
	adj.p=p.adjust(topGO.table[,"P-value"], method=P_ADJUST)
	topGO.table=cbind(topGO.table, adj.p)
	
	#sig.topGO.table=topGO.table[topGO.table[,"P-value"]<0.05,]
 	
 	#write.table(file=TOPGO_OUT, topGO.table, sep='\t', quote=FALSE, row.names=FALSE, col.names=FALSE)
	result=lapply(1:dim(topGO.table)[1], function(x){as.list(topGO.table[x,])})
	 	 	
 	})
}, binder=binder());





GENELIST=c('MOAP1', 
'CFLAR',
'BAX',
'AIFM3',
'PERP',
'BCL2L14',
'BFAR',
'BCL2L1',
'BCL2L2',
'FKSG2',
'BCL2L10',
'SIVA1',
'TP53BP2'
)


res=inject (function (gsea) gsea (GENELIST=GENELIST, BACKGROUND=BACKGROUND))		

#res=inject (function (nmf) nmf (in.mtx))		

 
