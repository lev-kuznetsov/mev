define(["ng", "./directives/projectAnalysisList.directive"], function(ng, ParojectAnalysisListDirective){
	var module=ng.module("mui.widgets.projectAnalysisList", []);
	module.directive("projectAnalysisList", ParojectAnalysisListDirective);
	return module;
});