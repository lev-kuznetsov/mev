"use strict";
define(["mui"], function(ng){
	function directive(){
		return {
			restrict: "A",
			transclude: true,
			// template: '<a data-toggle="modal" role="button" data-target="{{bsModalTrigger}}" ng-transclude></a>',
			template: "<ng-transclude></ng-transclude>",
			scope: {
				bsModalTrigger: "@mevBsModalTrigger",				
			},
			link: function(scope, elm, attrs){
				elm.attr("data-toggle", "modal")
				.attr("role", "button")
				.attr("data-target", scope.bsModalTrigger);
			}
		};
	}
	directive.$inject=[];
	directive.$name="mevBsModalTriggerDirective";
	return directive;
});