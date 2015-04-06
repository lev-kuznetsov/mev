define(["ng", "./modal/survivalModal.directive"], 
function(ng, SurvivalModalDirective){	
	var module = ng.module("mui.widgets.analysis.survival", ["mui.domain.analysis.survival"]);
	module.directive("survivalModal", SurvivalModalDirective);
	return module;
	
});