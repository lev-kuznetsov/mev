define(["ng", "./directives/projectAnalysisResultList.directive"], 
function(ng, ProjectAnalysisResultList){
	var module = ng.module("mui.widgets.projectAnalysisResultList", []);
	module.directive("projectAnalysisResultList", ProjectAnalysisResultList);
	return module;
});