define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.anova", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.controller("AnovaVM", ["$scope", "$state", "$stateParams", "project", "analysis", function($scope, $state, $stateParams, project, analysis){
		
		this.analysisId=$stateParams.analysisId;
		this.analysis=analysis;
		this.project=project;
		
	}]);
	return module;
});