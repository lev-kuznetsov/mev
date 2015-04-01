define(["ng", "lodash",
        "./controllers/SelectionSetsViewVM", 
        "./controllers/SelectionSetViewVM"], 
function(ng, _,
		SelectionSetsViewVM, 
		SelectionSetViewVM){
	
	var module=ng.module("mui.views.dataset.SelectionSets", []);
	
	module.controller("SelectionSetsViewVM", SelectionSetsViewVM);
	module.controller("SelectionSetViewVM", SelectionSetViewVM);
		
	return module;
});