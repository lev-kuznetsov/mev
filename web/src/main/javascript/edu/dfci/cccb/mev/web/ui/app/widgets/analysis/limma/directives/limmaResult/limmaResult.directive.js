define(["ng"], function(ng){
	var limmaResultDirective = function limmaResultDirective(){
		return {
			restrict: "AE",
			scope: {
				resultItem: "=",				
			},
			templateUrl: "app/widgets/analysis/limma/directives/limmaResult/limmaResult.table.tpl.html"
		}
	}
	limmaResultDirective.$inject=[];
	return limmaResultDirective;
});