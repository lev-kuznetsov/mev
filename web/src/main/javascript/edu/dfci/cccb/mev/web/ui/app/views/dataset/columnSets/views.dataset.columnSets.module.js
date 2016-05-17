define(["mui", "lodash", "./edit/views.dataset.columnSets.edit.module"],
function(ng, _){
	
	var module=ng.module("mui.views.dataset.columnSets", arguments, arguments);
	
	module.config(['$stateProvider', '$urlRouterProvider',
	   	     	function($stateProvider, $urlRouterProvider){	     				
	   	     		$stateProvider	   	     		
	   	     		.state("root.dataset.columnSets", {
	   	     			url: "columnSets",
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/columnSets/templates/views.dataset.columnSets.tpl.html",	   	     			
	   	     			controller: "SelectionSetsViewVM",
	   	     			controllerAs: "ColumnSetsViewVM",
	   	     			displayName: "column sets",
	   	     			resolve:{
	   	     				dimension: function(){	   	     				
	   	     					return "column";
	   	     				}
	   	     			}
	   	     		})
	   	     		.state("root.dataset.columnSet", {
	   	     			url: "columnSets/{setId}",
	   	     			params: {
	   	     				setId: "new"
	   	     			},
	   	     			displayName: "{{selectionSet.name}} column set",
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/columnSets/templates/views.dataset.columnSet.tpl.html",	   	     			
	   	     			controller: "SelectionSetViewVM",
	   	     			controllerAs: "ColumnSetViewVM",
	   	     			resolve:{
	   	     				selectionSet: ["$stateParams", "dataset", function($stateParams, dataset){
	   	     					var reset = dataset.resetSelections("column");
	   	     					console.debug("resolve ColumnSet", reset);
	   	     					
	   	     					return reset.$promise.then(function(){
		   	     					var columnSet = _.find(dataset.column.selections, function(selection){
		   	     						return selection.name === $stateParams.setId; 
		   	     					});
		   	     					
		   	     					if(!columnSet){
		   	     						columnSet = {name: "new", type: "column"};
		   	     					}
		   	     					
		   	     					console.debug("resolved ColumnSet", columnSet);
		   	     					return columnSet;
	   	     					});	   	     						   	     				
	   	     				}]
	   	     			}
	   	     		}
	   	     	);
	   	     		
	   	}]);	
	return module;
});