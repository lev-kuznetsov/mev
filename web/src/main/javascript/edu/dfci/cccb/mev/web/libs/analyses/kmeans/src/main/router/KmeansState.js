define(["./KmeansState.tpl.html"], function(template){ "use strict";
    function KmeansState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.kmeans", {
                parent: "root.dataset.analysisType",
                url: "kmeans/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "KmeansVMFactory",
                    function(scope, project, analysis, KmeansVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return KmeansVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "KmeansVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    KmeansState.inject=["$stateProvider"];
    KmeansState.provider="config";
    return KmeansState;
});