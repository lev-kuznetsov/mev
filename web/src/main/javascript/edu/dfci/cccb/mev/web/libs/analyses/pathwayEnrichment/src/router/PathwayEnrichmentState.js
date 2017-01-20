define(["./PathwayEnrichmentState.tpl.html"], function(template){ "use strict";
	function PathwayEnrichmentState($stateProvider){
		$stateProvider
	 		.state("root.dataset.analysisType.pe", {	 			
	 			parent: "root.dataset.analysisType",
	 			url: "pe/{analysisId}",
	 			template: template,	   	     			
	 			controller: ["$scope", "project", "analysis", "PathwayEnrichmentVMFactory", function(scope, project, analysis, PathwayEnrichmentVMFactory){
	 				scope.DatasetAnalysisVM = this;
	 				return PathwayEnrichmentVMFactory.call(this, scope, project, analysis);
	 			}],
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