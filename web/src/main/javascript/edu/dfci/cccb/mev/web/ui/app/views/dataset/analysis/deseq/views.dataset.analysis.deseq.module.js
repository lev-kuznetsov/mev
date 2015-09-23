define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.deseq", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("DESeqVMFactory", ["BoxPlotService", "projectionService", function(BoxPlotService, projectionService){
		return function($scope, project, analysis, BoxPlotService, projectionService){
			"use strict"
			var _self = this;
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
			console.debug("deseq DESeqVMFactory", $scope.$id, $scope.DatasetAnalysisVM);
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
			
			$scope.$on("ui:filteredResults", function($event, results){
				var control = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.control;});
				var experiment = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experiment;});
				
				$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(project.dataset, results, 
						[control, experiment], 
						analysis.randomId);
				
                var labels = results.map(projectionService.ids);                
				console.debug("deseq boxPloGenes", $scope.boxPlotGenes);
			});
		};
	}])
	.controller("DESeqVM", ["$scope", "$state", "$stateParams", "project", "analysis", "BoxPlotService", "projectionService", "DESeqVMFactory",
	                        function($scope, $state, $stateParams, project, analysis, BoxPlotService, projectionService, DESeqVMFactory){
		
			DESeqVMFactory.call(this, $scope, project, analysis, BoxPlotService, projectionService);			
	}]);
	return module;
});