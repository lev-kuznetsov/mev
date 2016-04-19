define(["mui", "angular-ui-router",
    "mev-analysis", "mev-domain-common", "mev-results-table", "mev-enrichment-dotplot",
    "./model/TopgoAnalysisType",
    "./router/TopgoState",
    "./router/TopgoVM"
], function(ng){
    return ng.module("mev-topgo", arguments, arguments);
})