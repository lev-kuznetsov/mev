define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.genesd", [])	
	.factory("GeneSDVMFactory", ["$stateParams", function($stateParams){
		return function GeneSDVMFactory($scope, $stateParams, project, analysis){
			this.analysisId=$stateParams.analysisId;
			this.analysis=analysis;
			this.project=project;
			$scope.dataset=project.dataset;
		};
	}])
	.controller("GeneSDVM", ["$scope", "$stateParams", "project", "analysis", "GeneSDVMFactory",
	                        function($scope, $stateParams, project, analysis, GeneSDVMFactory){
		GeneSDVMFactory.call(this, $scope, $stateParams, project, analysis);
	}]);
	return module;
});