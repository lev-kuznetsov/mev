/**
 * Created by antony on 4/13/16.
 */
define(["mui", "../data/gsea.json",
    "mev-dataset/src/main/dataset/lib/AnalysisClass",
    "mev-gsea",
    "angular-ui-router",
    "mev-mock",
    "bootstrap", "bootstrap/dist/css/bootstrap.min.css"], function(ng, analysisJson, AnalysisClass){
   var demo = ng.module("gsea-demo", arguments, arguments);
    demo.controller("demoCtrl", ["$scope", "mevGseaAnalysisType", function($scope, mevGseaAnalysisType){
        $scope.analysisType = mevGseaAnalysisType;
    }])
    .run(["$state", "mevMockProject", function($state, mevMockProject){
        mevMockProject.dataset.analyses.push(new AnalysisClass(analysisJson));
        $state.go("root.dataset.analysisType.gsea", {datasetId: mevMockProject.dataset.id, analysisId: analysisJson.name});
        // $state.go("mock");
    }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    });
});