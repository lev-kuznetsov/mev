shim ('multtest', callback = function (mt.teststat) {
  define ('anova', function () function (dataset, groups, pValue, correctForMultipleTesting) {
    if (is.null (names (groups))) names (groups) <- 1:length (groups);
    minValue <- min (dataset);
    if (minValue < 0) dataset <- dataset - minValue;
    dataset <- dataset[, tru] # FIXME in any of the groups
    
    groupMeans <- data.frame (row.names = rownames (dataset));
    for (group in groups) {
      groupMeans <- cbind (groupMeans, rowMeans (dataset[, group]));
    }
    groupMeans <- data.matrix (groupMeans);
    
    groupCount <- length (groups);
    
    numCols <- as.integer (groupCount * (groupCount - 1) / 2);
    
    lfcMatrix <- matrix (, nrow = dim (dataset)[ 1 ], ncol = numCols);
    
    previous <- 0;
    columnHeaders <- c ();
    
    for (i in 1:(groupCount - 1)) {
      lfcMatrix[, (previous + 1):(previous + groupCount - i)] <- groupMeans[, i] / groupMeans[, (i + 1):groupCount];
      previous <- previous + groupCount - i;
      
      for (j in (i + 1):groupCount) {
        columnHeaders <- c (columnHeaders, paste (names (groups)[i], '__MEV_VS__', names (groups)[j], sep = ''));
      }
    }
    
    log (lfcMatrix);
    colnames (lfcMatrix) <- columnHeaders;

    fStats <- mt.teststat (dataset, groupings, test = "f"); # FIXME groupings
    df1 <- groupCount - 1;
    df2 <- ncol (dataset) - groupCount;
    
    pVals <- 1 - pf (fStats, df1, df2);
    
    if (correctForMultipleTesting) {
      pVals <- p.adjust (pVals, method = "fdr");
    }
    
    results <- data.frame (rownames (dataset), pVals, lfcMatrix);
    
    apply (results, 1, function (row) {
      lfcRow = row[3:length (row)];
      changes <- list ();
      for (i in 1:length (lfcRow)) {
        partners <- strsplit (names (lfcRow)[i], '__MEV_VS__');
        changes <- c (changes, list (a = partners[ 1 ], b = partners[ 2 ], logFoldChange = lfcRow[ i ]));
      }
      list (geneId = row[ 1 ], pValue = row[ 2 ], changes = changes);
    });
  });
}, binder = binder ());
