define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.ttest", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.controller("tTestVM", ["$scope", "$state", "$stateParams", "BoxPlotService", "project", "analysis", 
	                        function($scope, $state, $stateParams, BoxPlotService, project, analysis){
		
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
		
		
		$scope.$on("ui:filteredResults",function($event, results){
			var control = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.controlName});
        	var experiment = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experimentName});
        	
       		$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(project.dataset, results, 
         		[control, experiment],
         		analysis.randomId);
		});
	}]);
	return module;
});