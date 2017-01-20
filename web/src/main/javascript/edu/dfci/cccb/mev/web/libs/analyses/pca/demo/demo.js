define(["mui", "mev-pca", "../data/pca_result_brcac.json!", 
	"../data/pca_result_lgg.json!", "../data/pca_result_small.json!", "../data/dataset_lgg.json!",
	"mev-dataset/src/main/dataset/lib/AnalysisClass",
	"angular-ui-bootstrap", "bootstrap/dist/css/bootstrap.css",
	"mev-analysis",
	"mev-mock",
	"bootstrap",
	"bootstrap/dist/css/bootstrap.min.css"
	], 
function(ng, pcamod,brca, lgg, small, dataset_lgg, AnalysisClass){"use strict";
	return ng.module("app", arguments, arguments)	
	.controller("pcaDemoCtrl", ["$scope", "mevPcaAnalysisType", function($scope, mevPcaAnalysisType){
		var _self = this;

		$scope.vm = {
			results: {
				"small": {result: small},
				"lgg": {result: lgg, dataset: dataset_lgg},
				"brca": {result: brca}
			},
			curResult: "lgg",
			showResult: function(){
				$scope.analysis = $scope.vm.results[$scope.vm.curResult];
			}
		};
		$scope.vm.showResult();
		$scope.vm.analysisType = mevPcaAnalysisType;
		console.debug("analysis", $scope.analysis);
	}])
	.run(["$state", "mevMockProject", function($state, mevMockProject){
		mevMockProject.dataset.analyses.push(new AnalysisClass(small));
		$state.go("root.dataset.analysisType.pca", {datasetId: mevMockProject.dataset.id, analysisId: small.name});
		// $state.go("mock");
	}]);
});
