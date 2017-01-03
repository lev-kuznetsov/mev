define(["./GeneMAD.tpl.html"], function(template){ "use strict";
    function GeneMADState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.genemad", {
                parent: "root.dataset.analysisType",
                url: "genemad/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "GeneMADVMFactory",
                    function(scope, project, analysis, GeneMADVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return GeneMADVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "GeneMADVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    GeneMADState.inject=["$stateProvider"];
    GeneMADState.provider="config";
    return GeneMADState;
});