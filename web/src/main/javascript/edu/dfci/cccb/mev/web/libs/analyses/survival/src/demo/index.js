define(["mui", "mev-survival", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/lgg.survival.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevSurvival, AnalysisClass, survivalJson){
    var app = ng.module("mev-survival-demmo", arguments, arguments)
        .controller("mevSurvivalDemoCtrl", ["$scope", "mevSurvivalAnalysisType", function($scope, mevSurvivalAnalysisType){
            $scope.vm = {
                analysisType: mevSurvivalAnalysisType
            };
        }])
        .config(["mevMockProjectProvider", function(mevMockProjectProvider){
            mevMockProjectProvider.datasetName = "lgg";
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(survivalJson));
            $state.go("root.dataset.analysisType.survival", {datasetId: mevMockProject.dataset.id, analysisId: survivalJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});