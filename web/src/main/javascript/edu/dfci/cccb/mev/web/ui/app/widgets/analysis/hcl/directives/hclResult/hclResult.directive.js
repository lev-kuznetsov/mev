define(["ng"], function(ng){
	var hclResultDirective = function hclResultDirective(){
		return {
			restrict: "AE",
			scope: {
				resultItem: "=",				
			},
			templateUrl: "app/widgets/analysis/hcl/directives/hclResult/hclResult.histogram.tpl.html"
		}
	}
	hclResultDirective.$inject=[];
	return hclResultDirective;
});