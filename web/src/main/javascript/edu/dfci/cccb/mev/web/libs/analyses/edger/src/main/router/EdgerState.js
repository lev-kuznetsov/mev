define(["./EdgerState.tpl.html"], function(template){ "use strict";
    function EdgerState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.edger", {
                parent: "root.dataset.analysisType",
                url: "edger/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "EdgerVMFactory",
                    function(scope, project, analysis, EdgerVMFactory, mevAnalysisTypes, BoxPlotService){
                    scope.DatasetAnalysisVM = this;
                    return EdgerVMFactory.call(this, scope, project, analysis, mevAnalysisTypes, BoxPlotService);
                }],
                controllerAs: "EdgerVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    EdgerState.inject=["$stateProvider"];
    EdgerState.provider="config";
    return EdgerState;
});