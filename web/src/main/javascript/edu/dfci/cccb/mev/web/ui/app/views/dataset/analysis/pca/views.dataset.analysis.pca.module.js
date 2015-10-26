define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.pca", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("PcaVMFactory", [function(){
		return function PcaVMFactory($scope, BoxPlotService, project, analysis){
			var _self = this;
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
		};
	}])
	.controller("PcaVM", ["$scope", "$injector", "BoxPlotService", "PcaVMFactory", "project", "analysis", 
	                        function($scope, $injector, BoxPlotService, PcaVMFactory, project, analysis){
		
		PcaVMFactory.call(this, $scope, BoxPlotService, project, analysis);
		
	}]);
	return module;
});