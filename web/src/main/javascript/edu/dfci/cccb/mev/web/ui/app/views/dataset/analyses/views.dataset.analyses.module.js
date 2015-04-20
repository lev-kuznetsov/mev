
define(["ng", "lodash"], 
		function(ng, _){
			
			var module=ng.module("mui.views.dataset.analyses", []);
			
			module.config(['$stateProvider', '$urlRouterProvider',
			   	     	function($stateProvider, $urlRouterProvider){	     				
			   	     		$stateProvider	   	     		
			   	     		.state("root.dataset.analyses", {
			   	     			url: "analyses",
			   	     			parent: "root.dataset",
			   	     			templateUrl: "app/views/dataset/analyses/templates/views.dataset.analyses.tpl.html",	   	     			
			   	     			controller: "AnalysesVM",
			   	     			controllerAs: "AnalysesVM",
			   	     			displayName: "analyses"
			   	     		}
			   	     	);			   	     		
			   	}]);	
			return module;
		});