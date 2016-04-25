define(["mui", "mev-edger", "./edger_mock.json",
    "mev-dataset/src/main/dataset/lib/AnalysisClass",
    "mev-mock",
    "bootstrap", "bootstrap/dist/css/bootstrap.min.css"], function(ng, mevEdger, edgerJson, AnalysisClass){"use strict";
    var demo = ng.module("mev-edger-demo", arguments, arguments)
    .controller("demoCtrl", ["$scope", "mevEdgerAnalysisType", function(scope, mevEdgerAnalysisType){
        scope.analysisType = mevEdgerAnalysisType;
    }])
    .run(["$state", "mevMockProject", function($state, mevMockProject){
        mevMockProject.dataset.analyses.push(new AnalysisClass(edgerJson));
        $state.go("root.dataset.analysisType.edger", {datasetId: mevMockProject.dataset.id, analysisId: edgerJson.name});
    }]);



    ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    })
});