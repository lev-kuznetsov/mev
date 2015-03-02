define(["ng", "./directives/projectAnalysis.directive"], function(ng, ProjectAnalysisDirective){
	var module=ng.module("mui.widgets.projectAnalysis", []);
	module.directive("projectAnalysis", ProjectAnalysisDirective);
	return module;
});