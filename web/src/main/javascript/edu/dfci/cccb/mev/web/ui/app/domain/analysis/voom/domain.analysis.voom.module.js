define(["ng", "./VoomVM"], 
function(ng, VoomVM){
	var module = ng.module("mui.domain.analysis.voom", []);
	module.service("VoomVM", VoomVM);
	return module;
});