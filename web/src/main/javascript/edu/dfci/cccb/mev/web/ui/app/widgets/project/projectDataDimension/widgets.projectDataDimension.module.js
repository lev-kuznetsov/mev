define(["ng", "./directives/projectDataDimension.directive"], function(ng, ProjectDataDirective){
	var module=ng.module("mui.widgets.projectDataDimension", []);
	module.directive("projectDataDimension", ProjectDataDirective);
	return module;
});