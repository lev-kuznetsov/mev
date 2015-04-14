define(["ng", "./controllers/DatasetsVM"], 
function(ng, DatasetsVM){
	var module = ng.module("mui.views.datasets", []);
	module.config(['$stateProvider', '$urlRouterProvider',
	     	function($stateProvider, $urlRouterProvider){	     						
	     		$stateProvider.state("root.datasets", {
	     			url: "/datasets",	     			
	     			controller: "ImportsCtrl",
	     			controllerAs: "ImportsCtrl",
	     			parent: "root",
	     			templateUrl: "app/views/datasets/templates/views.datasets.tpl.html",
	     			displayName: "datasets"
	     		});
	}]);
	module.controller("DatasetsVM", DatasetsVM);
	return module;
});
