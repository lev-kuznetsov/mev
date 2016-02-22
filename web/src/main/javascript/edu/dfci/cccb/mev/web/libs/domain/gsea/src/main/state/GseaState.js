define(["./GseaState.tpl.html"], function(template){ "use strict";
	function GseaState($stateProvider){
		$stateProvider
	 		.state("root.dataset.analysisType.gsea", {	 			
	 			parent: "root.dataset.analysisType",
	 			url: "gsea/{analysisId}",
	 			template: template,	   	     			
	 			controller: "GseaVM",
	 			controllerAs: "GseaVM",	 			
				displayName: "{{analysis.name}} analysis",
	 			resolve:{
	 				analysis: function($stateParams, dataset){
	 					return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });															
	 				}
	 			}
	 		});
	}
	GseaState.inject=["$stateProvider"];
	GseaState.provider="config";
	return GseaState;
});