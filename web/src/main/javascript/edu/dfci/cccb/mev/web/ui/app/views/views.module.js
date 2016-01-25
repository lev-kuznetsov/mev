define(["ng", "angular-ui-router", 
        "./root/views.root.module",        
        "./import/views.import.module",
        "./dataset/views.dataset.module",        
        "./welcome/views.welcome.module",
        "./datasets/views.datasets.module"], 
function(ng, ngrouter, root, importMod, datasetMod, welcomeMod, datasetsMod){
	var module = ng.module("mui.views", ["ui.router",
	                                     "mui.views.root",
	                                     "mui.views.import",
	                                     datasetMod.name,
	                                     welcomeMod.name,
	                                     datasetsMod.name]);	
	return module;
});