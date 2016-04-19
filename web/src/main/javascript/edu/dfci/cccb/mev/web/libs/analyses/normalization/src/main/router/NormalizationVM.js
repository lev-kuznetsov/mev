define(["lodash"], function(_){ "use strict";
    function NormalizationVM(mevAnalysisTypes){
        function factory($scope, project, analysis) {
            var _self = this;
            _self.project=project;
            _self.analysis=analysis;

        };
        factory.$inject=["$scope", "project", "analysis"];
        return factory;
    }
    NormalizationVM.$inject=["mevAnalysisTypes"];
    NormalizationVM.$name="NormalizationVMFactory";
    NormalizationVM.$provider="factory";
    return NormalizationVM;
});