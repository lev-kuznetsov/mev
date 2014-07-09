## Kruskal test

## input variables from MeV:
# KW_FILE: store a numeric vector
# GRP_FILE: store a group label vector

## output file
# KRUSKAL_OUT

data.vec = read.table(file=KW_FILE, header=FALSE)
data.vec = as.numeric(data.vec[,1])

group.vec = read.table(file=GRP_FILE, header=FALSE)
group.vec = as.vector(group.vec[,1])

k.test = kruskal.test(x=data.vec, g=as.factor(group.vec))
write.table(file=KRUSKAL_OUT, rbind(k.test$method, 'Kruskal-Wallis chi-squared'=k.test$statistic,'P-value'=k.test$p.value),
            sep='\t',col.names=FALSE,quote=FALSE)

## End Kruskal test