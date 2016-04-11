define(["ng", "./SelectionSetNode/selectionSetNode.directive"], 
function(ng, SelectionSetNodeDirective){
	var module=ng.module("mui.widgets.dataset.SelectionSet", []);		
	module.directive("selectionSetNode", SelectionSetNodeDirective);
	return module;
});