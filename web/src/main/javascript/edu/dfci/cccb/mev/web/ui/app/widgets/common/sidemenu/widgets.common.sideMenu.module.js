define(["ng", "./directives/sidemenu.directive", "./controllers/SideMenuVM", "./services/sidemenuSrvc", "./directives/toggleSidemenu.directive"], 
function(ng, SidemenuDirective, SideMenuVM, SideMenuSrvc, ToggleSidemenuDirective){
	var module=ng.module("mui.widgets.common.sideMenu", []);
	module.directive("sidemenu", SidemenuDirective);	
	module.controller("SideMenuVM", SideMenuVM);	
	module.service("SideMenuSrv", SideMenuSrvc);
	module.directive("toggleSidemenu", ToggleSidemenuDirective);	
	return module;
});