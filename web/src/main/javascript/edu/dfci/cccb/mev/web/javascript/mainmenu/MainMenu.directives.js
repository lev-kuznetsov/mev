define(["angular", "mainmenu/MainMenu.module"], function(angular, module){
	angular.module(module.name).directive("mevMainMenu", ['$routeParams', function($routeParams){
		return {
			scope:{},
			restrict: "AEC",
			transclude: true,
			templateUrl: module.path+"mainmenu.tpl.html",
			link: function(scope, elm, attr, nullCtrl){
			    
			    if ($routeParams.datasetName){
			        scope.brandLink = "#/datasets";
			    }
			    
			}
		};
	}]);
});