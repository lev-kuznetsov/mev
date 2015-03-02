define(["ng", "./DatasetResourceRepository"], 
function(ng, DatasetResourceRepository){
	var module=ng.module("mui.domain.dataset", []);
	module.service("DatasetRepository", ["$resource", DatasetResourceRepository]);
	return module;
})