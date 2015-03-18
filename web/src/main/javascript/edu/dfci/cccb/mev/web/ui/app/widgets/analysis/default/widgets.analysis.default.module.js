define(["ng", "./directives/defaultParameters.directive"], 
function(ng, DefaultParametersDirective){
	var module = ng.module("mui.widgets.analysis.default", []);
	module.directive("defaultParameters", DefaultParametersDirective);	
	return module;
});