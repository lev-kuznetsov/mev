define(["ng",
        "./common/widgets.common.module",
        "./mainmenu/mainmenu.module", 
        "./project/widgets.project.module",
        "./dataset/widgets.dataset.module",
        "./dashboard/widgets.dashboard.module"],
function(ng){
	var module=ng.module("mui.components", arguments, arguments);
	return module;
});