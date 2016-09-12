define(["ng", "angular-ui-router", 
        "./root/views.root.module",        
        "./import/views.import.module",
        "./dataset/views.dataset.module",        
        "./welcome/views.welcome.module",
        "./datasets/views.datasets.module"
],
function(ng, ngrouter, root, importMod, datasetMod, welcomeMod, datasetsMod){
	var module = ng.module("mui.views", arguments, arguments);
	return module;
});