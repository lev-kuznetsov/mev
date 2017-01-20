define(["lodash"], function(_){ "use strict";
	function PathwayEnrichmentVM(mevAnalysisTypes){
		function factory(scope, project, analysis){
			scope.project = project;
			scope.analysis = analysis;
			scope.selectedRows = {};
			scope.selectedGenes = [];
			var analysisType = mevAnalysisTypes.get("pe");
			if(analysisType && _.isFunction(analysisType.modelDecorator))
				analysisType.modelDecorator(analysis);
			scope.dotPlotConfig = {
				name: scope.analysis.name,
				data: scope.analysis.result,
				series: "Counts"
			}
			scope.selectedGenes = [];
			scope.selectedRows = [];
			scope.headers = [
				{
					'name': 'ID',
					'field': "ID",
					'icon': "search",
					'link': function (value) {
						return "http://www.reactome.org/content/detail/R-HSA-" + value;
					}
				},{
		           'name': 'Description',
		           'field': "Description",
		           'icon': "search",
		           'nowrap': true,
		           'check': function(value, row){
					   scope.selectedGenes.length = 0;
				        _.uniq(_.flatten(_.map(scope.selectedRows, function(item){
							return item.geneID.split(/[ \/]/).map(function(gene){return {id: gene};});
						})), "id")
						.map(function(gene){
							scope.selectedGenes.push(gene);
						});
			           	console.log("selectedGenes", scope.selectedGenes);
	           		}
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
		           'name': 'Count',
		           'field': "Count",
		           'icon': ["<=", ">="],
		           'datatype': "integer"
		       },{
		           'name': 'Gene Id',
		           'field': "geneID",
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
		factory.$inject=["$scope", "project", "analysis"];
		return factory;
	}	
	PathwayEnrichmentVM.$inject=["mevAnalysisTypes"];
	PathwayEnrichmentVM.$name="PathwayEnrichmentVMFactory";
	PathwayEnrichmentVM.$provider="factory";
	return PathwayEnrichmentVM;
});