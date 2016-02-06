"use strict";
define(["./PathwayEnrichmentState.tpl.html"], function(template){
	function PathwayEnrichmentState($stateProvider){
		$stateProvider	   	     		
	 		.state("root.dataset.analysis.pe", {	 			
	 			parent: "root.dataset.analysis",
	 			url: "PathwayEnrichment",
	 			template: template,	   	     			
	 			controller: "PathwayEnrichmentVM",
	 			controllerAs: "PathwayEnrichmentVM",	 			
				displayName: "{{analysis.name}} analysis",
	 			resolve:{
	 				
	 			}
	 		});
	}
	PathwayEnrichmentState.inject=["$stateProvider"];
	PathwayEnrichmentState.provider="config";
	return PathwayEnrichmentState;
});