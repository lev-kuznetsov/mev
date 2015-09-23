define(["ng", "./model/TcgaPreset"], 
function(ng, TcgaPreset){
	var module = ng.module("mui.domain.presets.tcga", ['js-data']);		
	module.factory("TcgaPreset", TcgaPreset);
	return module;
});