define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.deseq", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.controller("DESeqVM", ["$scope", "$state", "$stateParams", "project", "analysis", "BoxPlotService",
	                        function($scope, $state, $stateParams, project, analysis, BoxPlotService){
		
		this.analysisId=$stateParams.analysisId;
		this.analysis=analysis;
		this.project=project;
		
		project.generateView({
            viewType:'heatmapView', 
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
			var control = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.control});
        	var experiment = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experiment});
        	
			$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(project.dataset, results, 
            		[control, experiment], 
            		analysis.randomId);
            console.debug("deseq boxPloGenes", $scope.boxPlotGenes);
		});
	}]);
	return module;
});