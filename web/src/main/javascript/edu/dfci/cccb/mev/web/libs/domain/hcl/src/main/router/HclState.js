define(["./HclState.tpl.html", "./HclStateVM"], function(template, HclStateVM){ "use strict";
	function HclState($stateProvider){
		$stateProvider
	 		.state("root.dataset.analysisType.hcl", {	 			
	 			parent: "root.dataset.analysisType",
	 			url: "hcl/{analysisId}",
	 			template: template,	   	     			
	 			controller: ["$scope", "project", "analysis", "HclStateVMFactory", function(scope, project, analysis, HclStateVMFactory){
	 				return HclStateVMFactory.call(this, scope, project, analysis);
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
	HclState.inject=["$stateProvider"];
	HclState.provider="config";
	return HclState;
});