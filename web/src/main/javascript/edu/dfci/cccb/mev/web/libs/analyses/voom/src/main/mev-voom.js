define(["mui", "angular-ui-router", "mev-analysis", "mev-domain-common", "mev-gsea", "mev-pca", "mev-hcl", "mev-wgcna", 
    "mev-results-table", "mev-boxplot", "./model/VoomAnalysisType", "./router/VoomState", "./router/VoomVM"],
    function(ng){"use strict";
        return ng.module("mev-voom", arguments, arguments);
    });