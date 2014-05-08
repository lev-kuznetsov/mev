define(["angular", "mainmenu/MainMenu.module"], function(angular, module){
	angular.module(module.name).directive("mevMainMenu", [function(){
		return {
			scope:{},
			restrict: "AEC",
			transclude: true,
			templateUrl: module.path+"mainmenu.tpl.html",
			link: function(scope, elm, attr, nullCtrl){
				console.debug("linking MevMainMenu");
			}
		};
	}]);
});