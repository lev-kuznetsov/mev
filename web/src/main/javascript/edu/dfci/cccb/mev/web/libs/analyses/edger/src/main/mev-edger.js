define(["mui", "angular-ui-router", "mev-analysis", "mev-domain-common", "mev-gsea", "mev-pca", "mev-hcl", "mev-results-table", "mev-boxplot",
"./model/EdgerAnalysisType",
"./router/EdgerState",
"./router/EdgerVM.js"], function(ng){
    return ng.module("mev-edger", arguments, arguments);
});