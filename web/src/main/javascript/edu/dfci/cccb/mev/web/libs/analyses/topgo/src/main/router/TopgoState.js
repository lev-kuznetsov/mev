define(["./TopgoState.tpl.html"], function(template){ "use strict";
	function TopgoState($stateProvider){
		$stateProvider
	 		.state("root.dataset.analysisType.topgo", {
	 			parent: "root.dataset.analysisType",
	 			url: "topgo/{analysisId}",
	 			template: template,	   	     			
	 			controller: ["$scope", "project", "analysis", "TopgoVMFactory", function(scope, project, analysis, PathwayEnrichmentVMFactory){
	 				scope.DatasetAnalysisVM = this;
	 				return PathwayEnrichmentVMFactory.call(this, scope, project, analysis);
	 			}],
	 			controllerAs: "TopgoVM",
				displayName: "{{analysis.name}} analysis",
	 			resolve:{
	 				analysis: function($stateParams, dataset){
	 					return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });															
	 				}
	 			}
	 		});
	}
	TopgoState.inject=["$stateProvider"];
	TopgoState.provider="config";
	return TopgoState;
});