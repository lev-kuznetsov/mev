define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.histogram", [])	
	.factory("HistogramVMFactory", ["$stateParams", function($stateParams){
		return function HistogramVMFactory($scope, $stateParams, project, analysis){
			this.analysisId=$stateParams.analysisId;
			this.analysis=analysis;
			this.project=project;
			$scope.dataset=project.dataset;
		};
	}])
	.controller("HistogramVM", ["$scope", "$stateParams", "project", "analysis", "HistogramVMFactory",
	                        function($scope, $stateParams, project, analysis, HistogramVMFactory){
		HistogramVMFactory.call(this, $scope, $stateParams, project, analysis);
	}]);
	return module;
});