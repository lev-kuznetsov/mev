define([], function(){
	return function($scope, $stateParams, dashboard){
		var self=this;
		
		console.debug("DashboardCtrl $stateParams", $stateParams);		
//		console.debug("DashboardCtrl DashboardRepository", DashboardRepository);
		console.debug("DashboardCtrl dashboard", dashboard);
		console.debug("DashboardCtrl dashboard.getName()	", dashboard.getName());
		self.dashboard=dashboard;				
	};
});