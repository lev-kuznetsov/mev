"use strict";
define(["./PathwayEnrichmentState.tpl.html"], function(template){
	function PathwayEnrichmentState($stateProvider){
		$stateProvider
	 		.state("root.dataset.analysisType.pe", {	 			
	 			parent: "root.dataset.analysisType",
	 			url: "pe/{analysisId}",
	 			template: template,	   	     			
	 			controller: "PathwayEnrichmentVM",
	 			controllerAs: "PathwayEnrichmentVM",	 			
				displayName: "{{analysis.name}} analysis",
	 			resolve:{
	 				analysis: function($stateParams, dataset){
	 					return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });															
	 				}
	 			}
	 		});
	}
	PathwayEnrichmentState.inject=["$stateProvider"];
	PathwayEnrichmentState.provider="config";
	return PathwayEnrichmentState;
});