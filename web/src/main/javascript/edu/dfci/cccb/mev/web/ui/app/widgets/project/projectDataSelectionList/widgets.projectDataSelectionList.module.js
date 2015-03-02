define(["ng", "./directives/projectDataSelectionList.directive"], function(ng, ProjectDataSelectionListDirective){
	var module=ng.module("mui.widgets.projectDataSelectionList", []);
	module.directive("projectDataSelectionList", ProjectDataSelectionListDirective);
	return module;
});