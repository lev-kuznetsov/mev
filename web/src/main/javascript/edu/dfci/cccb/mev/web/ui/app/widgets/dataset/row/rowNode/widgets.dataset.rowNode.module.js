define(["ng", "./controllers/RowNodeVM", "./directives/rowNode.directive"], 
function(ng, RowNodeVM, RowNodeDirective){
	var module=ng.module("mui.widgets.dataset.RowNode", []);	
	module.controller("RowNodeVM", RowNodeVM);
	module.directive("rowNode", RowNodeDirective);
	return module;
});