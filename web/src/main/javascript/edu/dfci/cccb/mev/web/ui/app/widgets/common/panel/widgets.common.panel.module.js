define(["ng", "./directives/panel.directive", "./directives/panelControlls.directive", "./controllers/PanelVM", "./services/panelSrvc"], 
function(ng, PanelDirective, PanelControllsDirective, PanelVM, PanelSrvc){
	var module=ng.module("mui.widgets.common.panel", []);
	module.directive("panel", PanelDirective);
	module.directive("panelControlls", PanelControllsDirective);
	module.controller("PanelVM", PanelVM);	
	module.service("PanelSrv", PanelSrvc);
	return module;
});