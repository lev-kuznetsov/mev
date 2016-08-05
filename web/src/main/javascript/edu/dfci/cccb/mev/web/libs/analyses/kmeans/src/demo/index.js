define(["mui", "mev-kmeans", "mev-dataset/src/main/dataset/lib/AnalysisClass", "../data/mouse_test_data.kmeans.json",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.css",
    "angular-ui-bootstrap",
    "mev-analysis",
    "mev-mock"], function(ng, mevKmeans, AnalysisClass, kmeansJson){
    var app = ng.module("mev-kmeans-demmo", arguments, arguments)
        .controller("mevKmeansDemoCtrl", ["$scope", "mevKmeansAnalysisType", function($scope, mevKmeansAnalysisType){
            $scope.vm = {
                analysisType: mevKmeansAnalysisType
            };
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(kmeansJson));
            $state.go("root.dataset.analysisType.kmeans", {datasetId: mevMockProject.dataset.id, analysisId: kmeansJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
        ng.bootstrap(document, [app.name]);
    });
});