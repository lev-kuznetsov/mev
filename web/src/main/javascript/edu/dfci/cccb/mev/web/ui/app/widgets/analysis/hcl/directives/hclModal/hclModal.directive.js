define(["ng"], function(ng){
	var hclModalDirective = function hclModalDirective(){
		return {
			restrict: "AE",
			scope: {
				resultItem: "=",				
			},
			templateUrl: "app/widgets/analysis/hcl/directives/hclModal/hclModal.tpl.html"
		}
	}
	hclModalDirective.$inject=[];
	return hclModalDirective;
});