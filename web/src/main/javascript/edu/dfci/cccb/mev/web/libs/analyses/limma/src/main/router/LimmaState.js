define(["./LimmaState.tpl.html"], function(template){ "use strict";
    function LimmaState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.limma", {
                parent: "root.dataset.analysisType",
                url: "limma/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "LimmaVMFactory",
                    function(scope, project, analysis, LimmaVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return LimmaVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "LimmaVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    LimmaState.inject=["$stateProvider"];
    LimmaState.provider="config";
    return LimmaState;
});