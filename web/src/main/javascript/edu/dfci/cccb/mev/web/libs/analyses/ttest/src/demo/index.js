define(["mui", "mev-ttest", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/mouse_test_data.ttest.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevtTest, AnalysisClass, ttestJson){
    var app = ng.module("mev-ttest-demmo", arguments, arguments)
        .controller("mevtTestDemoCtrl", ["$scope", "mevtTestAnalysisType", function($scope, mevtTestAnalysisType){
            $scope.vm = {
                analysisType: mevtTestAnalysisType
            };
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(ttestJson));
            $state.go("root.dataset.analysisType.ttest", {datasetId: mevMockProject.dataset.id, analysisId: ttestJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});