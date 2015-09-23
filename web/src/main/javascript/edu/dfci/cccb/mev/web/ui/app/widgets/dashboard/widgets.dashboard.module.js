define(["ng", 
        "./directives/dashboardDirective", 
        "./directives/dashboardItemDirective", 
        "./directives/dashboardItemAddDirective",
        "./directives/dashboardItemRemoveDirective",
        "./controllers/DashboardVM"],        			
function(ng, DashboardDirective, DashboardItemDirective, DashboardItemAddDirective, DashboardVM){
	"use strict";
	console.debug("mui.widgets.dashboard");
	return ng.module("mui.widgets.dashboard", [], arguments);
});