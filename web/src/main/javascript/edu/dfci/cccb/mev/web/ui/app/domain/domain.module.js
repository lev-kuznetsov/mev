define(["mui", 
        "./navigator/domain.navigator.module",
        "./analysis/domain.analysis.module",
        "./presets/domain.presets.module",
        "./project/domain.project.module",
        "./dataset/domain.dataset.module"],                   
function(ng, navigatorMod, modAnalsys, modPresets, modProject, modDataset){
	var module = ng.module("mui.domain", arguments, arguments);
	return module;
});