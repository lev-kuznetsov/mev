define(["ng", "./controllers/IssuesVM"], 
function(ng, IssuesVM){
	var module = ng.module("mui.views.issues", []);
	module.config(['$stateProvider', '$urlRouterProvider',
	     	function($stateProvider, $urlRouterProvider){	     						
	     		$stateProvider.state("root.issues", {
	     			url: "/issues",	     			
	     			controller: "IssuesVM",
	     			controllerAs: "IssuesVM",
	     			parent: "root",
	     			templateUrl: "app/views/issues/templates/issues.tpl.html",	    				    			
	    			data: {
//	    				sidemenuUrl: "app/views/issues/templates/issues.sidemenu.tpl.html"
	    				sidemenuUrl: "app/views/issues/templates/issues.sidemenu.accordion.tpl.html"
	    			}
	     		});
	}]);
	module.controller("IssuesVM", IssuesVM);
	return module;
});
