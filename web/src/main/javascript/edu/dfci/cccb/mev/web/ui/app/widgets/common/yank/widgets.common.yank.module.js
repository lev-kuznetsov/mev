define(["ng", "./directives/yank.directive"], function(ng, YankDirective){
	var module = ng.module("mui.widgets.common.yank", []);
	module.directive("muiYank", YankDirective);
	return module;
});