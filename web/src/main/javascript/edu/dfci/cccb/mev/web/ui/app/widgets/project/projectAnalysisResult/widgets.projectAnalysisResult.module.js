define(["ng", "./directives/projectAnalysisResult.directive"], 
function(ng, ProjectAnalysisResult){
	var module = ng.module("mui.widgets.projectAnalysisResult", []);
	module.directive("projectAnalysisResult", ProjectAnalysisResult);
	return module;
});