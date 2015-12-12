define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.ttest", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("tTestVMFactory", [function(){
		return function tTestVMFactory($scope, BoxPlotService, project, analysis){
			var _self = this;
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
			
			
			$scope.$on("ui:resultsTable:filteredResults",function($event, results){
				var control = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.controlName;});
	        	var experiment = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experimentName;});
	        	var topResults = results.filter(function(item, index){
        			return index < _self.top;
        		});
	       		$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(project.dataset, topResults, 
	         		[control, experiment],
	         		analysis.randomId);
			});
			
			
			
        	this.top=5;
        	this.getTop=function(){
        		var control = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.controlName;});
            	var experiment = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experimentName;});
            	            	
        		var results = analysis.results.filter(function(item, index){
        			return index < _self.top;
        		});
        		$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(project.dataset, results, 
                 		[control, experiment],
                 		analysis.randomId);
        	};
       		
			
			
		};
	}])
	.controller("tTestVM", ["$scope", "$injector", "BoxPlotService", "tTestVMFactory", "project", "analysis", 
	                        function($scope, $injector, BoxPlotService, tTestVMFactory, project, analysis){
		
		tTestVMFactory.call(this, $scope, BoxPlotService, project, analysis);
		
	}]);
	return module;
});