define(["ng", "./tcga/domain.presets.tcga.module"], 
function(ng, presets){
	var module = ng.module("mui.domain.presets", [presets.name]);
	return module;
});