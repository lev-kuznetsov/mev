define(["lodash"], function(_){ "use strict";
	function GseaVM(scope, project, analysis){ 

		scope.project = project;
		scope.analysis = analysis;
		scope.selectedRows = {};
		scope.selectedGenes = [];
		scope.headers = [	       
	       {
	           'name': 'Description',
	           'field': "Description",
	           'icon': "search",
	           'nowrap': true	           
	       },{
	           'name': 'P-Value',
	           'field': "pvalue",
	           'icon': "<=",
	           'default': "0.05"
	       },{
	           'name': 'P-Adjust',
	           'field': "p.adjust",
	           'icon': "<="
	       },{
	           'name': 'q-Value',
	           'field': "qvalue",
	           'icon': "<="
	       },{
	           'name': 'Set Size',
	           'field': "setSize",
	           'icon': ["<=", ">="]
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
	}	
	GseaVM.$inject=["$scope", "project", "analysis"];
	GseaVM.$name="GseaVM";
	GseaVM.$provider="controller";
	return GseaVM;
});