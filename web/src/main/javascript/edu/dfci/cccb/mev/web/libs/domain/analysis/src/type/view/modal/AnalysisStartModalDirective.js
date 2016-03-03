"use strict";
define(["./AnalysisStartModal.tpl.html"], function(template){
	function AnalysisStartModalDirective(){
		return {
			restrict: "AEC",
			scope: {
				analysisType: "=mevAnalysisType"
			},
			template: template,
			controller: ["$scope", function(scope){
				this.id = scope.analysisType.id+"-start-analysis-modal";
			}],
			controllerAs: "StartAnalysisModalVM"

		};
	}
	AnalysisStartModalDirective.$inject=[];
	AnalysisStartModalDirective.$name="mevAnalysisStartModalDirective";
	AnalysisStartModalDirective.$provider="directive";
	return AnalysisStartModalDirective;
});