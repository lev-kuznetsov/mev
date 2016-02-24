define(["./PcaState.tpl.html"], function(template){ "use strict";
	function GseaState($stateProvider){
		$stateProvider
	 		.state("root.dataset.analysisType.pca", {	 			
	 			parent: "root.dataset.analysisType",
	 			url: "pca/{analysisId}",
	 			template: template,	   	     			
	 			controller: "PcaStateVM",
	 			controllerAs: "DatasetAnalysisVM",	 			
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