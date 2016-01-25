define(["mui", "mev-pca", "../data/pca_result_brcac.json!", 
	"../data/pca_result_lgg.json!", "../data/pca_result_small.json!", "../data/dataset_lgg.json!",
	"angular-ui-bootstrap", "bootstrap/dist/css/bootstrap.css"
	], 
function(ng, pcamod,brca, lgg, small, dataset_lgg){
	"use strict";
	return ng.module("app", arguments, arguments)	
	.controller("pcaDemoCtrl", ["$scope", function($scope){
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
		console.debug("analysis", $scope.analysis);
	}]);
});
