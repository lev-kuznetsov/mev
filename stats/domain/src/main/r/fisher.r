## Fisher's Exact test

## input variables from MeV:
# M,N,S,T: 4 non-negative integers to construc a 2x2 contingency table. M, N row 1; S, T row 2.
# ALT_HYPOTHESIS: 'two.sided'/'greater'/'less'. Default='two.sided'
# IS_SIMULATED: boolean. Default=FALSE

## output file
# FISHER_OUT

count.table = cbind(c(M,N), c(S,T))
f.test = fisher.test(x=count.table, alternative=ALT_HYPOTHESIS, simulate.p.value=IS_SIMULATED)
write.table(file=FISHER_OUT, rbind(f.test$method, 'Odds Ratio'=f.test$estimate, 'P-value'=f.test$p.value),
            sep='\t',col.names=FALSE,quote=FALSE)

## End Fisher's test