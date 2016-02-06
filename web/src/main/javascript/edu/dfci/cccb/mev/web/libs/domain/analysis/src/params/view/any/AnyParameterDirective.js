"use strict";
define(["./AnyParameter.tpl.html"], function(template){
	function AnyParameterDirective(){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			},
			
		}
	}
	AnyParameterDirective.$name="mevAnyParameterDirective"
	AnyParameterDirective.$inject=[];
	return AnyParameterDirective;
});