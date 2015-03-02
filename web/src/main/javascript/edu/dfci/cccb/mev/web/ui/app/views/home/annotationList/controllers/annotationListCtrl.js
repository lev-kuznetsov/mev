define([], function(){
	return function($scope, $state, AnnotationSetRepository, DashboardRepository, ProjectRepository){
		console.debug("ctrl AnnotationListDirective", $scope, $state, AnnotationSetRepository, DashboardRepository, ProjectRepository);		
		$scope.vm={
			gotoDashboard: function(ann){
//					DashboardRepository.create(ann);
//					console.debug("DashboardRepository.getAll()", DashboardRepository.getAll());
				$state.go("dashboard", {id:ann.meta.name}, {location: true});
			},
			all: [],
			dashs: []
		};		
		AnnotationSetRepository.getAll().then(function(annotationSets){
			$scope.vm.all=annotationSets;
		});
//		DashboardRepository.getAll().then(function(dashboards){
//			$scope.vm.dashs=dashboards;
//		});
		ProjectRepository.getAll().then(function(data){
			$scope.vm.projects=data;
		});	 
		
	};
});