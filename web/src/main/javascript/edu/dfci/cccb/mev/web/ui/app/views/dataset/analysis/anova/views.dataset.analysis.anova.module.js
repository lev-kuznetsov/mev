define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.anova", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("AnovaVMFactory", ["BoxPlotService", function(BoxPlotService){
		return function AnovaVMFactory($scope, BoxPlotService, project, analysis){
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
			
			this.heatmapView = project.generateView({
	            viewType:'heatmapView', 
	            note: analysis.name,
	            labels:{
	                row:{keys:project.dataset.row.keys}, 
	                column:{keys:project.dataset.column.keys}
	            },
	            expression:{
	                min: project.dataset.expression.min,
	                max: project.dataset.expression.max,
	                avg: project.dataset.expression.avg,
	            }
	        });			
		};
	}])
	.controller("AnovaVM", ["$scope", "project", "analysis", "BoxPlotService", "AnovaVMFactory",
	                        function($scope, project, analysis, BoxPlotService, AnovaVMFactory){
		AnovaVMFactory.call(this, $scope, BoxPlotService, project, analysis);
	}]);
	return module;
});