define(["./AnalysisStartButton.tpl.html"], function(template){"use strict";
	function directive(mevContext){
		return {
			restrict: "AEC",
			scope: {
				analysisType: "=mevAnalysisType",
				contextLevel: "@mevContextLevel"
			},
			template: template,
			controller: ["$scope", function(scope){
				this.id = scope.analysisType.id+"-start-analysis-modal";
				this.onClick=function(){					
					mevContext.setLevel(scope.contextLevel);
				};
			}],
			controllerAs: "StartAnalysisButtonVM"

		};
	}
	directive.$inject=["mevContext"];
	directive.$name="mevAnalysisStartButton";
	directive.$provider="directive";
	return directive;
});