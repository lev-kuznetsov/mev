define(["ng", "./controllers/DatasetLabelVM", "./directives/datasetLabel.directive"], 
function(ng, DatasetLabelVM, DatasetLabelDirective){
	var module=ng.module("mui.widgets.datasetlabel", []);	
	module.controller("DatasetLabelVM", DatasetLabelVM);
	module.directive("datasetLabel", [DatasetLabelDirective]);
	return module;
});