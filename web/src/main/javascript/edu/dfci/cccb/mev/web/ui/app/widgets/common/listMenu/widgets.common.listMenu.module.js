define(["ng", "./ListMenuVM", "./ListMenuDirective"], 
function(ng, ListMenuVM, ListMenuDirective){
	var module=ng.module("mui.widgets.common.listmenu", []);
	module.directive("listMenu", ListMenuDirective);
	module.controller("ListMenuVM", ListMenuVM);
	return module;
});