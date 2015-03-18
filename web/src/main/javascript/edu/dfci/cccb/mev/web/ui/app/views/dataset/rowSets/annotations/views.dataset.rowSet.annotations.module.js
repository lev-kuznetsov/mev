define(["ng", "./controllers/RowSetsAnnotationsViewVM"], 
function(ng, RowSetsAnnotationsViewVM){
	var module=ng.module("mui.views.dataset.rowSets.annotations", []);
	
	module.controller("RowSetsAnnotationsViewVM", RowSetsAnnotationsViewVM);
	module.config(['$stateProvider', '$urlRouterProvider',
	   	     	function($stateProvider, $urlRouterProvider){	     				
	   	     		$stateProvider	   	     		
	   	     		.state("root.dataset.rowSet.annotations", {
	   	     			url: "/dataset/:datasetId/rowSets/{setId}/annotations",
	   	     			params: {
	   	     				setId: null
			   	     	},
	   	     			parent: "root.dataset.rowSets",
	   	     			templateUrl: "app/views/dataset/rowSets/annotations/templates/views.dataset.rowSets.annotations.tpl.html",	   	     			
	   	     			controller: "RowSetsAnnotationsViewVM",
	   	     			controllerAs: "RowSetsAnnotationsViewVM",
	   	     			resolve:{
	   	     				selection: ["$stateParams", function($stateParams){
	   	     					
	   	     				}] 
	   	     			}
	   	     		});
	   	     		
	   	}]);	
	return module;
});