define(["ng", "lodash", "./controllers/RootCtrl", "mev-google-analytics"],
function(ng, _, RootCtrl){
	var module = ng.module("mui.views.root", ["mui.widgets.common"], arguments);
	return module;
});