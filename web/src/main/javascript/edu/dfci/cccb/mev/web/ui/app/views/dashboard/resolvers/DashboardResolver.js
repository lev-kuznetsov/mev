define([], function(){
	var DashboardResolver=function($stateParams, DashboardRepository){
		
		console.debug("resolving....", $stateParams, DashboardRepository);
		return DashboardRepository.exists($stateParams.id).then(function(dashboard){
			return dashboard;
		})["catch"](function(name){
			var dashboard = DashboardRepository.create($stateParams.id);
			return dashboard;
		});		
	};		
	
	return DashboardResolver;
});