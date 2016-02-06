"use strict";
define(["./AnalysisStartButton.tpl.html"], function(template){
	function directive(){
		return {
			restrict: "AEC",
			scope: {
				analysisType: "=mevAnalysisType"
			},
			template: template,
			controller: ["$scope", function(scope){
				this.id = scope.analysisType.id+"-start-analysis-modal";
			}],
			controllerAs: "StartAnalysisButtonVM"

		};
	}
	directive.$inject=[];
	directive.$name="mevAnalysisStartButton";
	directive.$provider="directive";
	return directive;
});