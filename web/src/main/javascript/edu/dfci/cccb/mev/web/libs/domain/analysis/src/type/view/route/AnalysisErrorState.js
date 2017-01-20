define(["lodash", "./AnalysisError.tpl.html"], function(_, template){"use strict";
	function state($stateProvider){
		$stateProvider	   	     					
	 		.state("root.dataset.analysisError", {
	 			parent: "root.dataset",
	 			url: "analysisError/{analysisId}",
	 			abstract: false,
	 			template: template,
	 			resolve:{
					analysis: ["$stateParams", "project", "dataset", function($stateParams, project, dataset){
						return _.find(dataset.analyses, function(analysis){ return analysis.name===$stateParams.analysisId; });
					}]
	 			},
				controller: ["analysis", function(analysis){
					this.analysis = analysis;
				}],
				controllerAs: "AnalysisErrorVM"
	 		});
	}
	state.inject=["$stateProvider"];
	state.provider="config";
	return state;
});