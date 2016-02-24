define(["./PcaState.tpl.html", "./PcaStateVM"], function(template, PcaStateVM){ "use strict";
	function PcaState($stateProvider){
		$stateProvider
	 		.state("root.dataset.analysisType.pca", {	 			
	 			parent: "root.dataset.analysisType",
	 			url: "pca/{analysisId}",
	 			template: template,	   	     			
	 			controller: ["$scope", "project", "analysis", "PcaStateVMFactory", function(scope, project, analysis, PcaStateVMFactory){
	 				return PcaStateVMFactory.call(this, scope, project, analysis);
	 			}],
	 			controllerAs: "DatasetAnalysisVM",	 			
				displayName: "{{analysis.name}} analysis",
	 			resolve:{
	 				analysis: function($stateParams, dataset){
	 					return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });															
	 				}
	 			}
	 		});
	}
	PcaState.inject=["$stateProvider"];
	PcaState.provider="config";
	return PcaState;
});