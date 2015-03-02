define(["ng", 
        "./templates/DashboardVM", 
        "./resolvers/DashboardResolver", 
//        "app/domain/dashboard/dashboard.module", 
        "nguirouter"], 
function(ng, DashboardVM, DashboardResolver){
	var module = ng.module("mui.views.dashboard", ["ui.router", "mui.dashboard"]);
	module.controller("DashboardVM", ["$scope", "$stateParams", "dashboard", DashboardVM]);
	module.config(['$stateProvider', function($stateProvider){
		
		$stateProvider.state("dashboard", {
			url: "/dashboard/:id",
			templateUrl: "app/views/dashboard/templates/dashboard.tpl.html",
			controller: "DashboardVM",
			controllerAs: "DashboardVM",
			resolve: {				
				dashboard: DashboardResolver
	//			dashboard: ["DashboardResolver", function(DashboardResolver){
	//				return DashboardResolver.resolve();
	//			}]
			}
		});
		
	}]);
	return module;
});