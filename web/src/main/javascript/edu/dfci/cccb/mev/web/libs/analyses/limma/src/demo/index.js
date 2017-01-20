define(["mui", "mev-limma", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/mouse_test_data.limma.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevLimma, AnalysisClass, limmaJson){
    var app = ng.module("mev-limma-demmo", arguments, arguments)
        .controller("mevLimmaDemoCtrl", ["$scope", "mevLimmaAnalysisType", function($scope, mevLimmaAnalysisType){
            $scope.vm = {
                analysisType: mevLimmaAnalysisType
            };
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(limmaJson));
            $state.go("root.dataset.analysisType.limma", {datasetId: mevMockProject.dataset.id, analysisId: limmaJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});