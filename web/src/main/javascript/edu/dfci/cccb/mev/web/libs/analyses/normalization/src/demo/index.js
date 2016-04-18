define(["mui", "mev-normalization", "./normalization_mock.json",
    "mev-dataset/src/main/dataset/lib/AnalysisClass",
    "mev-mock",
    "bootstrap", "bootstrap/dist/css/bootstrap.min.css"], function(ng, mevNormalization, normalizationJson, AnalysisClass){"use strict";
    var demo = ng.module("mev-normalization-demo", arguments, arguments)
    .controller("demoCtrl", ["$scope", "mevNormalizationAnalysisType", function(scope, mevNormalizationAnalysisType){
        scope.analysisType = mevNormalizationAnalysisType;
    }])
    .run(["$state", "mevMockProject", function($state, mevMockProject){
        mevMockProject.dataset.analyses.push(new AnalysisClass(normalizationJson));
        $state.go("root.dataset.analysisType.normalization", {datasetId: mevMockProject.dataset.id, analysisId: normalizationJson.name});
    }]);



    ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    })
});