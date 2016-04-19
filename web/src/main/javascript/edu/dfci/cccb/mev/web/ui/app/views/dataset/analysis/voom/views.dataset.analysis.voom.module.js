define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.dataset.analysis.voom", []);
	module.config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider){}])
	.factory("VoomVMFactory", [function(){
		return function voomVMFactory($scope, BoxPlotService, mevAnalysisTypes, project, analysis){
			var _self = this;
			this.analysisId=analysis.name;
			this.analysis=analysis;
			this.project=project;
			this.analysisTypes = mevAnalysisTypes.all();
			this.heatmapView = project.generateView({
	            viewType:'heatmapView',
	            note: analysis.name,
	            labels:{
	                row:{keys:project.dataset.row.keys}, 
	                column:{keys:project.dataset.column.keys}
	            }
	        });
//			{
//	            "logFC": 0.3858,
//	            "t": 9.5164,
//	            "_row": "Sell",
//	            "AveExpr": 14.5262,
//	            "P.Value": 1.5797e-15,
//	            "adj.P.Val": 6.1609e-14,
//	            "B": 24.7832
//	        },
			this.analysis.getFilteredKeys = function(dimension){
				if(dimension==="row")
					return _self.filteredResults.map(function(item){
						return item._row;
					});					
			};			
			this.analysis.results;
			this.analysis.results.getIdField=function(){
				return "_row";
			};
			this.analysis.results.getLogFoldChangeField=function(){
				return "logFC";
			}	

					
			this.analysis.getOriginalInputKeys=function(dimension){
				if(dimension==="column"){
					var selectionNames = [_self.analysis.params.$$experiment.name, _self.analysis.params.$$control.name];

					var keys = _self.project.dataset.selections.unionByName("column", selectionNames);
					keys.displayName = selectionNames.join("+");
					return keys;
				}

			};
			this.headers = [{
	                'name': 'ID',
	                'field': "_row",
	                'icon': "search"
	            },
	            {
	                'name': 'Log-Fold-Change',
	                'field': "logFC",
	                'icon': [">=", "<="]
	            },
	            {
	                'name': 'Average Expression',
	                'field': "AveExpr",
	                'icon': [">=", "<="]
	            },
	            {
	                'name': 'P-Value',
	                'field': "P.Value",
	                'icon': "<=",
                    'default': 0.05,
                    'max': 0.05,
                    'min': 0.00,
                    'step': 0.01
	            },
	            {
	                'name': 'q-Value',
	                'field': "adj.P.Val",
	                'icon': "<="
	            },
	            {
	            	'name': 't',
	            	'field': "t",
	            	'icon': "<="
	            },
	            {
	            	'name': 'B',
	            	'field': "B",
	            	'icon': "<="
	            }];
			this.udpateFilteredView = function (filteredResults) {
            	_self.filteredResults = filteredResults;
            	var labels = filteredResults.map(function(item){return item._row;});
            	_self.heatmapView = _self.heatmapView.applyFilter("row", labels);                
            };
            this.updatePageView = function (pageResults) {
            	_self.boxPlotGenes = BoxPlotService.prepareBoxPlotData(_self.project.dataset, pageResults, 
                		[_self.analysis.params.$$control, _self.analysis.params.$$experiment], 
                		_self.analysis.randomId, "_row");
            };
            $scope.$on("ui:resultsTable:pageChanged", function($event, results){            	
            });
			$scope.$on("ui:resultsTable:filteredResults",function($event, results){
//				var control = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.controlName;});
//	        	var experiment = _.find(project.dataset.column.selections, function(selection){return selection.name===analysis.params.experimentName;});
//	        	
//	       		$scope.boxPlotGenes = BoxPlotService.prepareBoxPlotData(project.dataset, results, 
//	         		[control, experiment],
//	         		analysis.randomId);
			});	
		};
	}])
	.controller("VoomVM", ["$scope", "$injector", "BoxPlotService", "VoomVMFactory","mevAnalysisTypes", "project", "analysis", 
	                        function($scope, $injector, BoxPlotService, VoomVMFactory, mevAnalysisTypes, project, analysis){
		
		VoomVMFactory.call(this, $scope, BoxPlotService, mevAnalysisTypes, project, analysis);
		
	}]);
	return module;
});