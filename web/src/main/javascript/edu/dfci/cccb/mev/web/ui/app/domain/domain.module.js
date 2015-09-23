define(["ng", 
        "./navigator/domain.navigator.module",
        "./analysis/domain.analysis.module",
        "./presets/domain.presets.module",
        "./project/domain.project.module",
        "./dataset/domain.dataset.module"],                   
function(ng, navigatorMod, modAnalsys, modPresets, modProject, modDataset){
	var module = ng.module("mui.domain", ["js-data", navigatorMod.name, modAnalsys.name, modPresets.name, modProject.name, modDataset.name]);
	module.config(function(DSProvider, DSHttpAdapterProvider){
//		ng.extend(DSProvider.defaults, { });
	    ng.extend(DSHttpAdapterProvider.defaults, { 
	    	basePath: "/",
	    	httpConfig: {
	    		params: {
	    			format: "json"
	    		}
	    	}
	    });
	});
	return module;
});