define(["./tTestState.tpl.html"], function(template){ "use strict";
    function tTestState($stateProvider){
        $stateProvider
            .state("root.dataset.analysisType.ttest", {
                parent: "root.dataset.analysisType",
                url: "tTest/{analysisId}",
                template: template,
                controller: ["$scope", "project", "analysis", "tTestVMFactory",
                    function(scope, project, analysis, tTestVMFactory){
                        scope.DatasetAnalysisVM = this;
                        return tTestVMFactory.call(this, scope, project, analysis);
                    }],
                controllerAs: "tTestVM",
                displayName: "{{analysis.name}} analysis",
                resolve:{
                    analysis: function($stateParams, dataset){
                        return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
                    }
                }
            });
    }
    tTestState.inject=["$stateProvider"];
    tTestState.provider="config";
    return tTestState;
});