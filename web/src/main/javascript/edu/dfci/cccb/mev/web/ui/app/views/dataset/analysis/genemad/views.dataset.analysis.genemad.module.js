define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.genemad", [])	
	.factory("GeneMADVMFactory", ["$stateParams", function($stateParams){
		return function GeneMADVMFactory($scope, $stateParams, project, analysis){
			this.analysisId=$stateParams.analysisId;
			this.analysis=analysis;
			this.project=project;
			$scope.dataset=project.dataset;
		};
	}])
	.controller("GeneMADVM", ["$scope", "$stateParams", "project", "analysis", "GeneMADVMFactory",
	                        function($scope, $stateParams, project, analysis, GeneMADVMFactory){
		GeneMADVMFactory.call(this, $scope, $stateParams, project, analysis);
	}]);
	return module;
});