define(["lodash"], function(_){ "use strict";
	function GseaVM(scope, project, analysis, mevAnalysisTypes, $timeout, $window){

		scope.project = project;
		scope.analysis = analysis;
		scope.selectedRows = {};
		scope.selectedGenes = [];
		var analysisType = mevAnalysisTypes.get("gsea");
		if(analysisType && _.isFunction(analysisType.modelDecorator))
			analysisType.modelDecorator(analysis);
		scope.barChartConfig = {
			name: scope.analysis.name,
			data: scope.analysis.result,
			series: "Score",
			x: {
				field: function(d){
					return d.Description;
				}
			},
			y: {
				field: "enrichmentScore",
				precision: 4,
				sort: "desc"
			},
			z: {
				field: "p.adjust"
			}
		}
		scope.headers = [	       
	       {
	           'name': 'Description',
	           'field': "Description",
	           'icon': "search",
	           'nowrap': true	           
	       },{
	           'name': 'P-Value',
	           'field': "pvalue",
	           'icon': ["<=", ">="],
	           'default': "0.05"
	       },{
	           'name': 'P-Adjust',
	           'field': "p.adjust",
	           'icon': ["<=", ">="]
	       },{
	           'name': 'q-Value',
	           'field': "qvalue",
	           'icon': ["<=", ">="]
	       },{
	           'name': 'Set Size',
	           'field': "setSize",
	           'icon': ["<=", ">="],
	           'datatype': "integer"
	       },{
	           'name': 'enrichmentScore',
	           'field': "enrichmentScore",
	           'icon': "search"
	       },

	  	];
	  	scope.getSelection=function(){
	  		return scope.selectedGenes;
	  	};
		scope.viewGenes = function (filteredResults){
		   	scope.filteredResults = filteredResults;
		  	// scope.applyToHeatmap(filteredResults);
		};
		scope.$on("$viewContentLoaded", function(event){
			console.log("$viewContentLoaded", arguments);
		});
		scope.activateTab=function($event){
			// console.log("ngClick", arguments);
			$timeout(function() {
				var evt = $window.document.createEvent('UIEvents');
				evt.initUIEvent('resize', true, false, $window, 0);
				$window.dispatchEvent(evt);
			});
		};
	}	
	GseaVM.$inject=["$scope", "project", "analysis", "mevAnalysisTypes", "$timeout", "$window"];
	GseaVM.$name="GseaVM";
	GseaVM.$provider="controller";
	return GseaVM;
});