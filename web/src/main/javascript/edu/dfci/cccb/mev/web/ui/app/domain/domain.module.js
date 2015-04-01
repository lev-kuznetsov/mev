define(["ng", 
        "./navigator/domain.navigator.module"], 
function(ng, navigatorMod){
	var module = ng.module("mui.domain", [navigatorMod.name]);
	return module;
});