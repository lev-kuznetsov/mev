define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.nmf", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("NmfVMFactory", [function(){
		return function NmfVMFactory($scope, project, analysis){
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
		};
	}])
	.controller("NmfVM", ["$scope", "$state", "$stateParams", "project", "analysis", "NmfVMFactory", 
	                      function($scope, $state, $stateParams, project, analysis, NmfVMFactory){
		NmfVMFactory.call(this, $scope, project, analysis);
	}]);
	return module;
});