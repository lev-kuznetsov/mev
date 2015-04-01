define(["ng", "lodash"], 
function(ng, _){
	
	var module=ng.module("mui.views.dataset.rowSets", []);
	
	module.config(['$stateProvider', '$urlRouterProvider',
	   	     	function($stateProvider, $urlRouterProvider){	     				
	   	     		$stateProvider	   	     		
	   	     		.state("root.dataset.rowSets", {
	   	     			url: "rowSets",
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/rowSets/templates/views.dataset.rowSets.tpl.html",	   	     			
	   	     			controller: "SelectionSetsViewVM",
	   	     			controllerAs: "RowSetsViewVM",
	   	     			resolve:{
		   	     			dimension: function(){
	   	     					return "row";
   	     					}
	   	     			}
	   	     		})
	   	     		.state("root.dataset.rowSet", {
	   	     			url: "rowSets/{setId}",
	   	     			params: {
	   	     				setId: "new"
	   	     			},
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/rowSets/templates/views.dataset.rowSet.tpl.html",	   	     			
	   	     			controller: "SelectionSetViewVM",
	   	     			controllerAs: "RowSetViewVM",
	   	     			resolve:{
	   	     				selectionSet: ["$stateParams", "dataset", function($stateParams, dataset){
	   	     					var reset = dataset.resetSelections("row");
	   	     					console.debug("resolve RowSet", reset);
	   	     					
	   	     					return reset.$promise.then(function(){
		   	     					var rowSet = _.find(dataset.row.selections, function(selection){
		   	     						return selection.name === $stateParams.setId; 
		   	     					});
		   	     					
		   	     					if(!rowSet){
		   	     						rowSet = {name: "new", type: "row"};
		   	     					}
		   	     					
		   	     					console.debug("resolved RowSet", rowSet);
		   	     					return rowSet;
	   	     					});	   	     						   	     				
	   	     				}]
	   	     			}
	   	     		}
	   	     	);
	   	     		
	   	}]);	
	return module;
});