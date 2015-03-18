define(["ng"], function(ng){
	var AnalysisModalDirective = function AnalysisModalDirective(){
		return {
			scope: {
				parameters: "="				
			},
			restrict: "AE",
			templateUrl: "app/widgets/analysis/default/templates/analysisModal.tpl.html"
			
		};
	};
	AnalysisModalDirective.$inject=[];
	return AnalysisModalDirective;
});