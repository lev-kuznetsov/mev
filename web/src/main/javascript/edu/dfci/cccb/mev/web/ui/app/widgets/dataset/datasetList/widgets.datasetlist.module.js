define(["ng", "./controllers/DatasetListVM", "./directives/datasetList.directive"], 
function(ng, DatasetListVM, datasetListDirective){
	var module=ng.module("mui.widgets.datasetlist", []);	
	module.controller("DatasetListVM", ["DatasetRepository", "Navigator", "$modal", "$scope", "$element", "$attrs", DatasetListVM]);
	module.directive("datasetList", [datasetListDirective]);
	return module;
});