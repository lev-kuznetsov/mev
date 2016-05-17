define(["ng", "lodash",
		"angular-color-picker", "angular-color-picker/angular-color-picker.css",
        "./controllers/SelectionSetsViewVM", 
        "./controllers/SelectionSetViewVM",
		"./resolvers/SelectionSetResolver",
		"./edit/SelectionSetsEditOnEnter",
		"./edit/SelectionSetsEditDirective"
],
function(ng, _,
		SelectionSetsViewVM, 
		SelectionSetViewVM){
	
	var module=ng.module("mui.views.dataset.SelectionSets", arguments, arguments);
	return module;
});