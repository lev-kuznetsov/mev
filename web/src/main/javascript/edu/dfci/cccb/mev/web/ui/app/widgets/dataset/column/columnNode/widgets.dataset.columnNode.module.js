define(["ng", "./controllers/ColumnNodeVM", "./directives/columnNode.directive"], 
function(ng, ColumnNodeVM, ColumnNodeDirective){
	var module=ng.module("mui.widgets.dataset.ColumnNode", []);	
	module.controller("ColumnNodeVM", ColumnNodeVM);
	module.directive("columnNode", ColumnNodeDirective);
	return module;
});