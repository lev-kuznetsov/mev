define(["ng", "./DashboardResolver"], 
function(ng, DashboardResolver){
	var module=ng.module("mui.resolvers", ["ui.router"]);
//	module.factory("DashboardResolver", ["$stateParams", "DashboardRepository", DashboardResolver]);
	return module;
});