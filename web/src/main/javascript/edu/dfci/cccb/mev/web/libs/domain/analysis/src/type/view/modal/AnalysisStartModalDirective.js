define(["./AnalysisStartModal.tpl.html", "./AnalysisStartModalHeader.tpl.html", "./AnalysisStartModal.less"],
function(template, headerTemplate){ "use strict";
	function AnalysisStartModalDirective($compile, $sce){
		return {
			restrict: "AEC",
			scope: {
				analysisType: "=mevAnalysisType"
			},
			template: template,
			controllerAs: "StartAnalysisModalVM",
			controller: ["$scope", "$transclude", function(scope){
				this.id = scope.analysisType.id+"-start-analysis-modal";
				this.hasInfo = (scope.analysisType.info && scope.analysisType.info.template);
				if(this.hasInfo){
					scope.headerHtml = $sce.trustAsHtml(scope.analysisType.info.template);
					var elemHeader = $compile(headerTemplate)(scope);
					this.headerHtml = elemHeader;
				}
			}],
			compile: function(tElem, tAttrs, transclude){
				return {
					pre: function(scope, elem, attrs, ctrl){
						if(ctrl.hasInfo){
							var elemInfo = elem.find(".mev-analysis-type-info");
							elemInfo.html(scope.analysisType.info.template);
						}
					},
					post: function(scope, elem, attrs, ctrl){

					}
				};
			},
		};
	}
	AnalysisStartModalDirective.$inject=["$compile", "$sce"];
	AnalysisStartModalDirective.$name="mevAnalysisStartModal";
	AnalysisStartModalDirective.$provider="directive";
	return AnalysisStartModalDirective;
});