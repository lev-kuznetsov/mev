define(["ng", "./controllers/WelcomeVM"], 
function(ng, WelcomeVM){
	var module = ng.module("mui.views.welcome", []);
	module.config(['$stateProvider', '$urlRouterProvider',
	     	function($stateProvider, $urlRouterProvider){	     						
	     		$stateProvider.state("root.welcome", {
	     			url: "/welcome",	     			
	     			controller: "WelcomeVM",
	     			controllerAs: "WelcomeVM",
	     			parent: "root",
	     			templateUrl: "app/views/welcome/templates/views.welcome.tpl.html",
	     			displayName: "mev"
	     		});
	}]);
	module.controller("WelcomeVM", WelcomeVM);
	return module;
});
