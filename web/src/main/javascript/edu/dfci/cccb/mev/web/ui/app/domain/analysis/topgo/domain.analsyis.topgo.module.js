define(["ng", "./TopGoVM"], 
function(ng, TopGoVM){
	var module = ng.module("mui.domain.analysis.topgo", []);
	module.service("TopGoVM", TopGoVM);
	return module;
});