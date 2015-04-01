define(["ng",
        "./sidepanel/widgets.common.sidePanel.module",
        "./sidemenu/widgets.common.sideMenu.module",
        "./panel/widgets.common.panel.module",
        "./layout/widgets.common.layout.module",
        "./yank/widgets.common.yank.module.js"],
function(ng){
	var module=ng.module("mui.widgets.common", [
	                                            "mui.widgets.common.sidePanel",
	                                            "mui.widgets.common.sideMenu",
	                                            "mui.widgets.common.panel",
	                                            "mui.widgets.common.layout",
	                                            "mui.widgets.common.yank"]);
	return module;
});