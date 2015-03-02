define(["ng", "../dal/DataRepositoryMixin", "./ProjectResourceRepository"], 
function(ng, DataRepositoryMixin, ProjectResourceRepository){
	var module=ng.module("mui.domain.project", []);
//	module.factory("ProjectRepository", ["$http", function($http){
//		function ProjectRepository(){
//			this.data=[];
//		};
//		DataRepositoryMixin.call(ProjectRepository.prototype, {url: "api/project", $http: $http,
//			getId: "id",
//			setId: "id"});
//		
//		return new ProjectRepository();		
//	}]);
//	
	module.service("ProjectResourceRepository", ["$resource", ProjectResourceRepository]);
	module.service("ProjectRepository", ["$resource", ProjectResourceRepository]);
	return module;
})