define(["mui", "angular-ui-router", "mev-heatmap", "mev-analysis", "mev-domain-common",
    "./model/KmeansAnalysisType", "./router/KmeansState", "./router/KmeansVM"],
    function(ng){"use strict";
        return ng.module("mev-kmeans", arguments, arguments);
    });