define(["ng", "./directives/dashboard.directive"], function(ng, dashboardDirective){
	var module = ng.module("mui.widgets.common.dashboard", []);
	module.directive("muiDashboard", dashboardDirective);
	return module;
});