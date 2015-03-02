define([], function(){
	return function DashboardMenuCtrl($scope, $state, DashboardRepository){
		console.log("DashboardMenuCtrl - init", $state.$current.name, $state.includes('dashboard'));
		$scope.vm={};
		DashboardRepository.getAll().then(function(dashboards){
			$scope.vm.dashboards=dashboards;	
		});
		$scope.$state=$state;
		
	};
});