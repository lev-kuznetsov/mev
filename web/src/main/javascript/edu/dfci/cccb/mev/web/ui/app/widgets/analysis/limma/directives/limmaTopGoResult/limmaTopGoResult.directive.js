define(["ng"], function(ng){
	var limmaTopGoResultDirective = function limmaTopGoResultDirective(){
		return {
			restrict: "AE",
			scope: {
				resultItem: "=",				
			},
			templateUrl: "app/widgets/analysis/limma/directives/limmaTopGoResult/limmaTopGoResult.tpl.html"
		}
	}
	limmaTopGoResultDirective.$inject=[];
	return limmaTopGoResultDirective;
});