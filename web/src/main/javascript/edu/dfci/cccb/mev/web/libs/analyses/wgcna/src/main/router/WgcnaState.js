define(["./WgcnaState.tpl.html"], function(template){ "use strict";
    function WgcnaState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.wgcna", {
                parent: "root.dataset.analysisType",
                url: "wgcna/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "WgcnaVMFactory",
                    function(scope, project, analysis, WgcnaVMFactory, mevAnalysisTypes, BoxPlotService){
                        scope.DatasetAnalysisVM = this;
                        return WgcnaVMFactory.call(this, scope, project, analysis, mevAnalysisTypes, BoxPlotService);
                    }],
                controllerAs: "WgcnaVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    WgcnaState.inject=["$stateProvider"];
    WgcnaState.provider="config";
    return WgcnaState;
});