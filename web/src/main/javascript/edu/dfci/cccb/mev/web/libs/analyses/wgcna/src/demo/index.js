define(["mui", "mev-wgcna",
    "../data/wgcna_mouse_result.json",
    "mev-dataset/src/main/dataset/lib/AnalysisClass",
    "mev-bs-modal",
    "mev-mock",
    "bootstrap",
    "bootstrap/dist/css/bootstrap.min.css"],
function(ng, mevWgcna, wgcnaMouseJson, AnalysisClass){
   
    var demo = ng.module("mev-wgcna-demo", arguments, arguments)
        .controller("demoCtrl", ["$scope", "mevWgcnaAnalysisType", function(scope, mevWgcnaAnalysisType){
            scope.mevWgcnaAnalysisType = mevWgcnaAnalysisType;
        }])
        .run(["$state", "mevMockProject", function($state, mevMockProject){
            mevMockProject.dataset.analyses.push(new AnalysisClass(wgcnaMouseJson));
            $state.go("root.dataset.analysisType.wgcna", {datasetId: mevMockProject.dataset.id, analysisId: wgcnaMouseJson.name});
            // $state.go("mock");
        }]);

    ng.element(document).ready(function(){
       ng.bootstrap(document, [demo.name]);
    });
});