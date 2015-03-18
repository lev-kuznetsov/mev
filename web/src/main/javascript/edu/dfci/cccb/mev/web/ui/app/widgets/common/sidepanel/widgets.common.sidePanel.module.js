define(["ng", "./directives/sidepanel.directive", "./directives/toggleSidepanel.directive","./services/sidepanelSrvc", "./controllers/ToggleSidepanelVM"], 
function(ng, SidepanelDirective, ToggleSidepanelDirective, SidepanelSrvc, ToggleSidepanelVM){
	var module=ng.module("mui.widgets.common.sidePanel", []);
	module.directive("sidepanel", SidepanelDirective);
	module.directive("toggleSidepanel", ToggleSidepanelDirective);
	module.controller("ToggleSidepanelVM", ToggleSidepanelVM);
	module.service("sidepanelSrvc", SidepanelSrvc);
	return module;
});