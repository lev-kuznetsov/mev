define(["mui", "angular-ui-router", "mev-analysis", "mev-domain-common", "mev-gsea", "mev-pca", "mev-hcl", "mev-wgcna", 
    "mev-results-table", "mev-boxplot", "./model/tTestAnalysisType", "./router/tTestState", "./router/tTestVM"],
function(ng){"use strict";
    return ng.module("mev-ttest", arguments, arguments);
});