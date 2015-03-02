define(["ng", "./controllers/ColumnSetsAnnotationsViewVM"], 
function(ng, ColumnSetsAnnotationsViewVM){
	var module=ng.module("mui.views.dataset.columnSets.annotations", []);
	
	module.controller("ColumnSetsAnnotationsViewVM", ColumnSetsAnnotationsViewVM);
	module.config(['$stateProvider', '$urlRouterProvider',
	   	     	function($stateProvider, $urlRouterProvider){	     				
	   	     		$stateProvider	   	     		
	   	     		.state("root.dataset.columnSet.annotations", {
	   	     			url: "/dataset/:datasetId/columnSets/{setId}/annotations",
	   	     			params: {
	   	     				setId: null
			   	     	},
	   	     			parent: "root.dataset.columnSets",
	   	     			templateUrl: "app/views/dataset/columnSets/annotations/templates/views.dataset.columnSets.annotations.tpl.html",	   	     			
	   	     			controller: "ColumnSetsAnnotationsViewVM",
	   	     			controllerAs: "ColumnSetsAnnotationsViewVM",
	   	     			resolve:{
	   	     				selection: ["$stateParams", function($stateParams){
	   	     					
	   	     				}] 
	   	     			}
	   	     		});
	   	     		
	   	}]);	
	return module;
});