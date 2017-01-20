define(["mui", "mev-edger", "./edger_mock.json",
    "mev-dataset/src/main/dataset/lib/AnalysisClass",
    "mev-gsea", "mev-pca", "mev-hcl", "mev-wgcna",
    "mev-mock",
    "bootstrap", "bootstrap/dist/css/bootstrap.min.css"], function(ng, mevEdger, edgerJson, AnalysisClass){"use strict";
    var demo = ng.module("mev-edger-demo", arguments, arguments)
    .config(["mevMockProjectProvider", function(mevMockProjectProvider){
        mevMockProjectProvider.datasetName = "raw_count_matrix.rand.primary.counts.1000.tsv";
    }])
    .controller("demoCtrl", ["$scope", "mevEdgerAnalysisType", "mevAnalysisTypes", function(scope, mevEdgerAnalysisType, mevAnalysisTypes){
        scope.analysisType = mevEdgerAnalysisType;
        scope.analysisTypes = mevAnalysisTypes;
    }])
    .run(["$state", "mevMockProject", function($state, mevMockProject){
        mevMockProject.dataset.analyses.push(new AnalysisClass(edgerJson));
        $state.go("root.dataset.analysisType.edger", {datasetId: mevMockProject.dataset.id, analysisId: edgerJson.name});
    }]);



    ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    })
});