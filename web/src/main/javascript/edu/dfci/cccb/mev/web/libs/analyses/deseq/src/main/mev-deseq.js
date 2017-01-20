define(["mui", "angular-ui-router", "mev-analysis", "mev-domain-common", "mev-pca", "mev-hcl", "mev-wgcna", 
    "mev-results-table", "mev-boxplot", "./model/DeseqAnalysisType", "./router/DeseqState", "./router/DeseqVM"],
function(ng){"use strict";
    return ng.module("mev-deseq", arguments, arguments);
});