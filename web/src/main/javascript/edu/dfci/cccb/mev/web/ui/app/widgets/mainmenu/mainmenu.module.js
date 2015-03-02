define(["ng", "./directives/mainmenu.directive", "./controllers/mainmenu.controller"], function(ng, MainMenuDirective, MainMenuController){
//	console.debug("mainmenu.module init - MainMenuDirective", MainMenuDirective);
	var module = ng.module("mui.components.mainmenu", []);	
	module.directive("mainMenu", MainMenuDirective);
//	module.controller("MainMenuController", MainMenuController);
	return module;
});