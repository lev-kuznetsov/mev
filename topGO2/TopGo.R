library(topGO)
library (injectoR)


#########
#
# GENELIST   Gene list of interest	 string vector
# SPECIES							 string					options: "human", "mouse"
# GO_TYPE							 string					options: "BP", "CC", "MF"
# TEST_TYPE							 string     			options: 'fisher', 'ks_test'
# P_ADJUST							 string 				options: "fdr", "hochberg", "bonferroni", "BH", "fdr", "none"
# NODE_SIZE	 Num of GO term to return	int 				options: 1 to 100



# library(biomaRt)
# GENERATE BACKGROUND GENE LISTS
# HUMAN GENES
# ensembl=useMart("ensembl")
# ensembl = useMart("ensembl",dataset="hsapiens_gene_ensembl")
# genes=getBM(c('entrezgene','hgnc_symbol'), mart=ensembl)#   , filters='with_go_id', values='GO:0004707', mart=ensembl)
# BACKGROUND=genes[,2] # Human Background
# write.csv(unique(BACKGROUND), file="HSAPIENS_ALL_GENES.txt", row.names=FALSE, quote=FALSE)
# # mouse genes
# ensembl=useMart('ensembl', dataset='mmusculus_gene_ensembl')
# genes=getBM(c('entrezgene','hgnc_symbol'), mart=ensembl)#   , filters='with_go_id', values='GO:0004707', mart=ensembl)
# BACKGROUND=genes[,2] # Mouse Background
# write.csv(unique(BACKGROUND), file="MMUSCULUS_ALL_GENES.txt", row.names=FALSE, quote=FALSE)


shim('topGO', callback=function (){
	define(gsea = function() function(
		GENELIST, 
	SPECIES='human', 
	GO_TYPE='BP', 
	TEST_TYPE='fisher',
	P_ADJUST='fdr', 
	NODE_SIZE=100){
 	if(SPECIES=='human'){
			library(org.Hs.eg.db)
			anno.db='org.Hs.eg.db'
			BACKGROUND=as.vector(read.csv(file="./HSAPIENS_ALL_GENES.txt")[,1])
	}else if(SPECIES=='mouse'){
			library(org.Mm.eg.db)
			anno.db='org.Mm.eg.db'
			BACKGROUND=as.vector(read.csv(file="./MMUSCULUS_ALL_GENES.txt")[,1])
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
 				nodeSize=NODE_SIZE,
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
 	if (totalNodes >= NODE_SIZE){
 	 	topGO.table = GenTable(myGOdata, topGO.result, topNodes=NODE_SIZE)}
 	if (totalNodes < NODE_SIZE){
   		topGO.table = GenTable(myGOdata, topGO.result, topNodes=totalNodes)}
   		
    colnames(topGO.table) = c('GO ID','GO Term','Annotated Genes','Significant Genes','Expected','P-value')
	adj.p=p.adjust(topGO.table[,"P-value"], method=P_ADJUST)
	topGO.table=cbind(topGO.table, adj.p)
	
	#sig.topGO.table=topGO.table[topGO.table[,"P-value"]<0.05,]
 	
 	#write.table(file=TOPGO_OUT, topGO.table, sep='\t', quote=FALSE, row.names=FALSE, col.names=FALSE)
	result=lapply(1:dim(topGO.table)[1], function(x){as.list(topGO.table[x,])})
	 	 	
 	})
}, binder=binder());

 
# CASE EXAMPLE
#

# GENELIST=c('MOAP1', 
# 'CFLAR',
# 'BAX',
# 'AIFM3',
# 'PERP',
# 'BCL2L14',
# 'BFAR',
# 'BCL2L1',
# 'BCL2L2',
# 'FKSG2',
# 'BCL2L10',
# 'SIVA1',
# 'TP53BP2'
# )


# res=inject (function (gsea) gsea (GENELIST=GENELIST, 
# 	SPECIES='human', 
# 	GO_TYPE='MF', 
# 	TEST_TYPE='fisher',
# 	P_ADJUST='fdr', 
# 	NODE_SIZE=100))

