define(["mui", "mev-topgo",
        "./topgo_mock.json",
        "mev-dataset/src/main/dataset/lib/AnalysisClass",
        "mev-bs-modal",
        "mev-results-table",
        "mev-mock",
        "bootstrap", "bootstrap/dist/css/bootstrap.min.css"
    ],
    function(ng, mevTopgo, topgoJson,  AnalysisClass){"use strict";
        var demo = ng.module("demo", arguments, arguments)
            .controller("demoCtrl", ["$scope", "mevTopgoAnalysisType", function(scope, mevTopgoAnalysisType){
                scope.mevTopgoAnalysisType = mevTopgoAnalysisType;
            }])
            .run(["$state", "mevMockProject", function($state, mevMockProject){
                mevMockProject.dataset.analyses.push(new AnalysisClass(topgoJson));
                $state.go("root.dataset.analysisType.topgo", {datasetId: mevMockProject.dataset.id, analysisId: topgoJson.name});
            }]);
        ng.element(document).ready(function(){
            ng.bootstrap(document, [demo.name]);
        });
    });