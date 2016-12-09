define(["ng", "./controllers/WelcomeVM", "ngtweet"],
function(ng, WelcomeVM){
	var module = ng.module("mui.views.welcome", ["ngtweet"]);
	module.config(['$stateProvider', "$$animateJsProvider",
	     	function($stateProvider, $$animateJsProvider){
	     		$stateProvider.state("root.welcome", {
	     			url: "/welcome",	     			
	     			controller: "WelcomeVM",
	     			controllerAs: "WelcomeVM",
	     			parent: "root",
	     			templateUrl: "app/views/welcome/templates/views.welcome.tpl.html",
	     			displayName: "mev",
					onEnter: ["mevFetchSrc", function(mevFetchSrc) {
						return mevFetchSrc.fetch("app/views/dataset/views.dataset.module", $$animateJsProvider)
							.catch(function(e){
								throw e;
							});
					}]
	     		});
	}]);
	module.controller("WelcomeVM", WelcomeVM);
	return module;
});
