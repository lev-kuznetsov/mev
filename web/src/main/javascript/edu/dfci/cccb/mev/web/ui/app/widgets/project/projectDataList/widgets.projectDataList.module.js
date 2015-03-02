define(["ng", "./directives/projectDataList.directive"], function(ng, ProjectDataListDirective){
	var module=ng.module("mui.widgets.projectDataList", []);
	module.directive("projectDataList", ProjectDataListDirective);
	return module;
});