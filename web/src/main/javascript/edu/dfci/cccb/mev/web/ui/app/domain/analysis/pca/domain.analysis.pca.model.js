define(["ng", "./Pca"], 
function(ng, PcaVM){
	var module = ng.module("mui.domain.analysis.pca", []);
	module.service("PcaVM", PcaVM);
	return module;
});