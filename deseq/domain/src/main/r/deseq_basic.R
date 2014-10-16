
if(!require("DESeq", character.only=T)) stop("Please install the DESeq package first.")

# input parameters will be added to this script by the R script-loading mechanism
# input parameters are as follows:
# COUNT_MTX_FILE: path to a file (with a header line) which has a gene column followed by N sample-specific columns.
#                 Each row has the data for a particular gene.  The header line has the sample IDs, which must match those
#                 of the sample file.  This way the samples can accurately map to the biological state.  
# SAMPLE_FILE: a two-column (tab-separated) file with the sample and biological state.
# CONDITION_A, CONDITION_B: strings indicating which biological states we are contrasting.  These should match the designated states in the second column of the SAMPLE_FILE.  CONDITION_A is considered to be the 'control' sample
# OUTPUT_FILE: full path to the output file
# NORMALIZED_COUNTS_FILE: path to the file with the normalized read counts

# read in the count matrix:
count_data <- read.table(COUNT_MTX_FILE, header=T, sep="\t")
count_data <- na.omit(count_data)

#name the rows by the genes and remove that column of the dataframe
rownames(count_data)<-count_data[,1]
count_data<-count_data[-1]

# read the sample to group mappings:
sample_groups_tuple <- read.table(SAMPLE_FILE, sep="\t")
sample_groups_tuple[,1] <- make.names(sample_groups_tuple[,1]) #in case the names contain characters like '-'.  Makes invalid variable/sample names into valid ones

# get ALL the conditions from the sample file:
all_conditions <- factor(sample_groups_tuple[,2])

#make sure the conditions passed to the script match at least one of the conditions annotated:
if (!(CONDITION_A %in% all_conditions) || !(CONDITION_B %in% all_conditions)){
	stop("Could not find samples corresponding to the desired contrast.")
}

#keep only those samples which are in the current contrast:
sample_groups_tuple <- sample_groups_tuple[sample_groups_tuple[,2] %in% c(CONDITION_A, CONDITION_B),]

# parse out the sample IDs and cast as a character array for sorting on the columns of the count data
sample_ids <- as.character(sample_groups_tuple[,1])

# parse out the conditions and save as a factor:
conditions <- factor(sample_groups_tuple[,2])

# change the order of the data matrix columns to match those of the conditions factor:
count_data <- count_data[, sample_ids]

#run the actual DESeq steps:
cds=newCountDataSet(count_data, conditions)
cds=estimateSizeFactors(cds)
cds=estimateDispersions(cds)
res=nbinomTest(cds, CONDITION_A, CONDITION_B)

#write out the normalized counts:
nc<-counts( cds, normalized=TRUE )
write.table(as.data.frame(nc), file=NORMALIZED_COUNTS_FILE, row.names=TRUE, col.names=F, quote=F, sep="\t")

#write the differential expression results to a file:
write.table(as.data.frame(res), file=OUTPUT_FILE, row.names=FALSE, col.names=F, quote=F, sep="\t")
