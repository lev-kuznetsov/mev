define(["./AnovaState.tpl.html"], function(template){ "use strict";
    function AnovaState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.anova", {
                parent: "root.dataset.analysisType",
                url: "anova/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "AnovaVMFactory",
                    function(scope, project, analysis, AnovaVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return AnovaVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "AnovaVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    AnovaState.inject=["$stateProvider"];
    AnovaState.provider="config";
    return AnovaState;
});