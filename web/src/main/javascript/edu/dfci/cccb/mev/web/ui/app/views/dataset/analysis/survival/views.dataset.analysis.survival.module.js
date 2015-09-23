define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.survival", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("SurvivalVMFactory", ["$stateParams", function($stateParams){
		return function SurvivalVMFactory($scope, $stateParams, project, analysis){
			this.analysisId=$stateParams.analysisId;
			this.analysis=analysis;
			this.project=project;
			$scope.dataset=project.dataset;
		};
	}])
	.controller("SurvivalVM", ["$scope", "$stateParams", "project", "analysis", "SurvivalVMFactory",
	                        function($scope, $stateParams, project, analysis, SurvivalVMFactory){
		SurvivalVMFactory.call(this, $scope, $stateParams, project, analysis);
	}]);
	return module;
});