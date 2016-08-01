define(["mui", "angular-ui-router", "mev-analysis", "mev-domain-common", "mev-time-function", "mev-annotations",
    "./model/SurvivalAnalysisType",
    "./model/SurvivalAnalysisSrv",
    "./model/SurvivalColumnList",
    "./router/SurvivalState",
    "./router/SurvivalVM"],
    function(ng){"use strict";
        return ng.module("mev-survival", arguments, arguments);
    });