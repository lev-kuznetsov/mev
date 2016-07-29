define(["mui", "mev-voom", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/mouse_test_data.voom.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevVoom, AnalysisClass, voomJson){
    var app = ng.module("mev-voom-demmo", arguments, arguments)
        .controller("mevVoomDemoCtrl", ["$scope", "mevVoomAnalysisType", function($scope, mevVoomAnalysisType){
            $scope.vm = {
                analysisType: mevVoomAnalysisType
            };
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(voomJson));
            $state.go("root.dataset.analysisType.voom", {datasetId: mevMockProject.dataset.id, analysisId: voomJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});