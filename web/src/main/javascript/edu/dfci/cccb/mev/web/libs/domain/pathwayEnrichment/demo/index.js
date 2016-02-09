"use strict";
define(["mui", "mevPathwayEnrichment",
	"../data/mouse_test_data_pe.json", 
	"../data/mouse_test_data_limma.json", 
	"mev-dataset/src/main/dataset/lib/AnalysisClass",
	"mev-bs-modal",
	"mev-results-table",	
	"mev-mock",	
	"bootstrap", "bootstrap/dist/css/bootstrap.min.css"
	], 
	function(ng, mevPathwayEnrichment, peJson, limmaJson, AnalysisClass){
	var demo = ng.module("demo", arguments, arguments)
	.controller("demoCtrl", ["$scope", "mevPathwayEnrichmentAnalysisType", function(scope, PathwayEnrichmentAnalysisType){
		scope.PathwayEnrichmentAnalysisType = PathwayEnrichmentAnalysisType;

		scope.headers = [	       
	       {
	           'name': 'Description',
	           'field': "Description",
	           'icon': "search",
	           'nowrap': true,
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
	           'name': 'Q-Value',
	           'field': "qvalue",
	           'icon': "<="
	       },{
	           'name': 'Count',
	           'field': "Count",
	           'icon': ["<=", ">="]
	       },{
	           'name': 'ID',
	           'field': "geneID",
	           'icon': "search"
	       },

	   ];
	   scope.viewGenes = function (filteredResults){        	
	    	scope.filteredResults = filteredResults;
	    	// scope.applyToHeatmap(filteredResults);
	   };
       scope.analysis = peJson;
	}])
	.run(["$state", "mevMockProject", function($state, mevMockProject){
		mevMockProject.dataset.analyses.push(new AnalysisClass(peJson));
		$state.go("root.dataset.analysisType.pe", {datasetId: mevMockProject.dataset.id, analysisId: peJson.name});
		// $state.go("mock");
	}])
	// .service("mevSelectionLocator", function(){
	
	// 	this.find = function(){
	// 		return [{name: "s1", x: 1},{name: "s2", x: 2}, {name: "s3", x: 3}];		
	// 	};			
		
	// })
	.config([function($stateProvider){
		// $stateProvider.state("root.dataset.analysis");
	}]);	
	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});