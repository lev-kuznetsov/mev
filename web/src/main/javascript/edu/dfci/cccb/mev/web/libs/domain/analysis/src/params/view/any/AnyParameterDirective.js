define(["./AnyParameter.tpl.html"], function(template){ "use strict";
	function AnyParameterDirective(){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			},
			
		};
	}
	AnyParameterDirective.$name="mevAnyParameterDirective";
	AnyParameterDirective.$inject=[];
	return AnyParameterDirective;
});