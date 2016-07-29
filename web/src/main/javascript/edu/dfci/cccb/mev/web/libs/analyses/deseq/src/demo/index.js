define(["mui", "mev-deseq", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/deseq_test_data.deseq.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevDeseq, AnalysisClass, deseqJson){
    var app = ng.module("mev-deseq-demmo", arguments, arguments)
        .controller("mevDeseqDemoCtrl", ["$scope", "mevDeseqAnalysisType", function($scope, mevDeseqAnalysisType){
            $scope.vm = {
                analysisType: mevDeseqAnalysisType
            };
        }])
        .config(["mevMockProjectProvider", function(mevMockProjectProvider){
            mevMockProjectProvider.datasetName = "deseq_test_data.tsv";
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(deseqJson));
            $state.go("root.dataset.analysisType.deseq", {datasetId: mevMockProject.dataset.id, analysisId: deseqJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});