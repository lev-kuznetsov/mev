## Wilcoxon Rank Sum test

## input variables from MeV:
# SAMPLE1_FILE: store a numeric vector
# SAMPLE2_FILE: optional, store a numeric vector. Default=NULL
# ALT_HYPOTHESIS: 'two.sided'/'greater'/'less'. Default='two.sided'
# IS_PAIRED: boolean. Default=FALSE
# IS_CONF_INT: boolean. Default=FALSE

## output file
# WILCOX_OUT

sample1 = read.table(file=SAMPLE1_FILE, header=FALSE)
sample1 = as.numeric(sample1[,1])

if (!is.null(SAMPLE2_FILE)){
  sample2 = read.table(file=SAMPLE2_FILE, header=FALSE)
  sample2 = as.numeric(sample2)
}

w.test = wilcox.test(x=sample1, y=sample2, alternative=ALT_HYPOTHESIS, paired=IS_PAIRED, conf.int=IS_CONF_INT)
write.table(file=WILCOX_OUT, rbind(w.test$method, 'W statistics'=w.test$statistic, 'P-value'=w.test$p.value),
            sep='\t',col.names=FALSE,quote=FALSE)

## End Wilcoxon test