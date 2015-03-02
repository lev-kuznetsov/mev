define(["ng", "lodash", "./controllers/RootCtrl"], 
function(ng, _, RootCtrl){
	var module = ng.module("mui.views.root", ["mui.widgets.common"]);			
	module.controller(RootCtrl.$name, RootCtrl);	
	return module;
});