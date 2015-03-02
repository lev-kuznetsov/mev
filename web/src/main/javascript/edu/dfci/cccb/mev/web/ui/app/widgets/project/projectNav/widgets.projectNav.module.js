define(["ng", "./directives/projectNav.directive"],
function(ng, ProjectNavDirective){
	var module=ng.module("mui.widgets.projectNav", []);
	module.directive("projectNav", ProjectNavDirective);
	return module;
});