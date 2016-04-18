define(["./NormalizationState.tpl.html"], function(template){ "use strict";
    function NormalizationState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.normalization", {
                parent: "root.dataset.analysisType",
                url: "normalization/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "NormalizationVMFactory", function(scope, project, analysis, NormalizationVMFactory){
                    scope.DatasetAnalysisVM = this;
                    return NormalizationVMFactory.call(this, scope, project, analysis);
                }],
                controllerAs: "NormalizationVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    NormalizationState.inject=["$stateProvider"];
    NormalizationState.provider="config";
    return NormalizationState;
});