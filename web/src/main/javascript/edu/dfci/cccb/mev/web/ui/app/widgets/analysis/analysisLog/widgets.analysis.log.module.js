define(["ng", 
        "./directives/analysisLog.directive"], 
function(ng, AnalsysLogDirective){
	var module = ng.module("mui.widgets.analysis.log", []);
	module.directive("analysisLog", AnalsysLogDirective);	
	return module;
});