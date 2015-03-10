define(["ng", "./controllers/DatasetNodeVM", "./directives/datasetNode.directive"], 
function(ng, DatasetNodeVM, DatasetNodeDirective){
	var module=ng.module("mui.widgets.dataset.DatasetNode", []);	
	module.controller("DatasetNodeVM", DatasetNodeVM);
	module.directive("datasetNode", DatasetNodeDirective);
	return module;
});