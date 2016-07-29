define(["./VoomState.tpl.html"], function(template){ "use strict";
    function VoomState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.voom", {
                parent: "root.dataset.analysisType",
                url: "voom/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "VoomVMFactory",
                    function(scope, project, analysis, VoomVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return VoomVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "VoomVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    VoomState.inject=["$stateProvider"];
    VoomState.provider="config";
    return VoomState;
});