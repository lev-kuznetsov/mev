define(["ng"],function(ng){
	var module=ng.module("mui.domain.navigator", []);
	module.factory("Navigator", ["$state", function($state){
		function Navigator(){
			this.openProject=function(project){
				$state.go("root.project", {id:project.id});
			}
			this.newProject=function(){
				$state.go("root.project");
			}
			this.importDataset=function(datasource, dataset){
				$state.go("root.import", {datasourceId: datasource.id, datasetId: dataset.id});
			}
			this.goHome=function(){
				$state.go("root.home");
			}
			this.openDataset=function(){
//				$state.go
			}
			this.openAnnotations=function(dataset, dimension){
				$state.go("root.dataset.annotations", {datasetId: dataset.id, dimension: dimension});
			}
		}
		return new Navigator();
	}]);
	return module;
});