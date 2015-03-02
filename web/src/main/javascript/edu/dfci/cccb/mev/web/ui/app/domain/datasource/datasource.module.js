define(["ng", "../dal/DataRepositoryMixin", "./DatasourceResourceRepository"], 
function(ng, DataRepositoryMixin, DatasourceResourceRepository){
	var module=ng.module("mui.domain.datasource", []);
	
	module.factory("DatasourceRepository", ["$http", function($http){
		function DatasourceRepository(){
			this.data=[];
		};
		DataRepositoryMixin.call(DatasourceRepository.prototype, {url: "api/datasource", $http: $http});
		return new DatasourceRepository();		
	}]);

	module.service("DatasourceRepository", ["$resource", DatasourceResourceRepository]);
	return module;
})