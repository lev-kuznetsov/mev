define(["./SurvivalState.tpl.html"], function(template){ "use strict";
    function SurvivalState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.survival", {
                parent: "root.dataset.analysisType",
                url: "survival/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "SurvivalVMFactory",
                    function(scope, project, analysis, SurvivalVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return SurvivalVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "SurvivalVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    SurvivalState.inject=["$stateProvider"];
    SurvivalState.provider="config";
    return SurvivalState;
});