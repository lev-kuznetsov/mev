## Skillings-Mack test

## input variables from MeV:
# SM_FILE: store the numeric matrix, and column/row labels.
#          1st column is unique row labels; 1st row from 2nd column on is unique column labels.
#          Columns as samples, rows as genes. It can have 'missing' values (e.g. 'NA' in R).
# IS_SIMULATED: boolean. Default=FALSE

## output file
# SM_OUT

library(Skillings.Mack)

data.mat = read.table(file=SM_FILE, sep='\t', header=TRUE, row.names=1)
data.mat = as.matrix(data.mat)

s.test = capture.output(SkiMack(y=t(data.mat), simulate.p.value=IS_SIMULATED))

write.table(file=SM_OUT, s.test[1:3], quote=FALSE, row.names=FALSE, col.names=FALSE)

## End Skillings-Mack test