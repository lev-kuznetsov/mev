define(["mui", "mev-anova", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/mouse_test_data.anova.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevAnova, AnalysisClass, anovaJson){
    var app = ng.module("mev-anova-demmo", arguments, arguments)
        .controller("mevAnovaDemoCtrl", ["$scope", "mevAnovaAnalysisType", function($scope, mevAnovaAnalysisType){
            $scope.vm = {
                analysisType: mevAnovaAnalysisType
            };
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(anovaJson));
            $state.go("root.dataset.analysisType.anova", {datasetId: mevMockProject.dataset.id, analysisId: anovaJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});