define(["./DeseqState.tpl.html"], function(template){ "use strict";
    function DeseqState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.deseq", {
                parent: "root.dataset.analysisType",
                url: "deseq/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "DeseqVMFactory",
                    function(scope, project, analysis, DeseqVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return DeseqVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "DeseqVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    DeseqState.inject=["$stateProvider"];
    DeseqState.provider="config";
    return DeseqState;
});