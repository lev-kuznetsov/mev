"use strict";
define(["mui", 
	"../data/mouse_test_data_limma.json",
	"../data/mouse_test_data_pe.json",
	"bootstrap/dist/css/bootstrap.min.css",
	"mev-results-table"], function(ng, limmaJson){
	var demo = ng.module("demo", arguments, arguments)
	.controller("demoCtrl", ["$scope", function(scope){

		scope.headers = [
	       {
	           'name': 'ID',
	           'field': "id",
	           'icon': "search"
	       },
	       {
	           'name': 'Log-Fold-Change',
	           'field': "logFoldChange",
	           'icon': [">=", "<="]
	       },
	       {
	           'name': 'Average Expression',
	           'field': "averageExpression",
	           'icon': [">=", "<="]
	       },
	       {
	           'name': 'P-Value',
	           'field': "pValue",
	           'icon': ["<=", ">="],
	           'default': 0.05
	       },
	       {
	           'name': 'Q-Value',
	           'field': "qValue",
	           'icon': ["<=", ">="]
	       }
	   ];
	   scope.viewGenes = function (filteredResults){        	
	    	scope.filteredResults = filteredResults;
	    	// scope.applyToHeatmap(filteredResults);
	   };
       scope.analysis = limmaJson;

	}]);


	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});