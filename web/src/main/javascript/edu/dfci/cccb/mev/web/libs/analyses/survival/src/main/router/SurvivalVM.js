define(["lodash"], function(_){ "use strict";
    function SurvivalVM(mevAnalysisTypes, $stateParams){
        function factory($scope, project, analysis){
            var _self=this;
            this.analysisId=$stateParams.analysisId;
            this.analysis=analysis;
            this.project=project;
            $scope.dataset=project.dataset;
        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    SurvivalVM.$inject=["mevAnalysisTypes", "$stateParams"];
    SurvivalVM.$name="SurvivalVMFactory";
    SurvivalVM.$provider="factory";
    return SurvivalVM;
});