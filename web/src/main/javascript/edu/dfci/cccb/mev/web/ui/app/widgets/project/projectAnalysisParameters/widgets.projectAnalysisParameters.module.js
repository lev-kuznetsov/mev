define(["ng", "./directives/projectAnalysisParameters.directive"], function(ng, ProjectAnalysisParametersDirective){
	var module=ng.module("mui.widgets.projectAnalysisParameters", []);
	module.directive("projectAnalysisParameters", ProjectAnalysisParametersDirective);
	return module;
});