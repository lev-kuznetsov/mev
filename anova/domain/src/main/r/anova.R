shim ('multtest', function (mt.teststat) {
  define ('anova', function () function (dataset, group, pValue) {
    minValue <- min (dataset);
    if (minValue < 0) dataset <- dataset + minValue;
    
  });
}, binder = binder ());