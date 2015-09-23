define(["ng",
        "./common/widgets.common.module",
        "./mainmenu/mainmenu.module", 
        "./project/widgets.project.module",
        "./dataset/widgets.dataset.module",
        "./analysis/widgets.analysis.module",
        "./presets/widgets.presets.module",
        "./dashboard/widgets.dashboard.module"],
function(ng){
	var module=ng.module("mui.components", ["ui.router",
	     "mui.widgets.common",
	     "mui.widgets.mainmenu", 
	     "mui.widgets.project",
	     "mui.widgets.dataset",
	     "mui.widgets.analysis", 
	     "mui.widgets.presets",		     
	     "mui.widgets.dashboard"]);	
	return module;
});