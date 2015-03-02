define(["ng", "./directives/projectData.directive"], function(ng, ProjectDataDirective){
	var module=ng.module("mui.widgets.projectData", []);
	module.directive("projectData", ProjectDataDirective);
	return module;
});