define(["mui", "angular-ui-router", "mev-analysis", "mev-domain-common",  "mev-heatmap",
        "mev-results-table", "mev-boxplot", "./model/AnovaAnalysisType", "./router/AnovaState", "./router/AnovaVM"],
    function(ng){"use strict";
        return ng.module("mev-anova", arguments, arguments);
    });