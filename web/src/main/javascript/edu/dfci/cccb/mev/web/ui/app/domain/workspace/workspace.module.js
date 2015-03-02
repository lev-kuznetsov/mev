define(["ng"], function(ng, DataRepositoryMixin){
	var module=ng.module("mui.domain.workspace", []);
	module.factory("Workspace", ["ProjectRepository", "DatasourceRepository", function(ProjectRepository, DatasourceRepository){
		function Workspace(){
			this.getPprojects=function(){
				return ProjectRepository.getAll();
			};
			this.getPproject=function(name){
				return ProjectRepository.get(name);
			};
			this.getDatasources=function(){
				return DatasourceRepository.getAll();
			};			
			this.getDatasource=function(name){
				return DatasourceRepository.get(name);
			};
		}
		return new Workspace();		
	}]);
		
	return module;
});