define(["ng", "./_controllers/DatasetsVM"], 
function(ng, DatasetsVM){
	var module = ng.module("mui.views.datasets", ["mui.widgets.presets"]);
	module.config(['$stateProvider', '$urlRouterProvider',
	     	function($stateProvider, $urlRouterProvider){	     						
	     		$stateProvider.state("root.datasets", {
	     			url: "/datasets",	     			
	     			controller: "ImportsCtrl",
	     			controllerAs: "ImportsCtrl",
	     			parent: "root",
	     			templateUrl: "app/views/datasets/_templates/views.datasets.tpl.html",
	     			displayName: "datasets"
	     		});
	}]);
	module.controller("DatasetsVM", DatasetsVM);
	return module;
});
