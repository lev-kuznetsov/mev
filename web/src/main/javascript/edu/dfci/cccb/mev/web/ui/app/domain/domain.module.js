define(["ng", 
        "./navigator/domain.navigator.module",
        "./analysis/domain.analysis.module"], 
function(ng, navigatorMod){
	var module = ng.module("mui.domain", [navigatorMod.name, "mui.domain.analysis"]);
	return module;
});