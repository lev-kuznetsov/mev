define(["mui", "mev-genemad", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/mouse_test_data.genemad.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevGeneMAD, AnalysisClass, GeneMADJson){
    var app = ng.module("mev-genemad-demmo", arguments, arguments)
        .controller("mevGeneMADDemoCtrl", ["$scope", "mevGeneMADAnalysisType", function($scope, mevGeneMADAnalysisType){
            $scope.vm = {
                analysisType: mevGeneMADAnalysisType
            };
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(GeneMADJson));
            $state.go("root.dataset.analysisType.genemad", {datasetId: mevMockProject.dataset.id, analysisId: GeneMADJson.name});
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});