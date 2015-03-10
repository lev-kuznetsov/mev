define(["ng", "lodash",
        "./controllers/ColumnSetsViewVM", 
        "./controllers/ColumnSetViewVM"], 
function(ng, _,
		ColumnSetsViewVM, 
		ColumnSetViewVM){
	
	var module=ng.module("mui.views.dataset.columnSets", []);
	
	module.controller("ColumnSetsViewVM", ColumnSetsViewVM);
	module.controller("ColumnSetViewVM", ColumnSetViewVM);
	module.config(['$stateProvider', '$urlRouterProvider',
	   	     	function($stateProvider, $urlRouterProvider){	     				
	   	     		$stateProvider	   	     		
	   	     		.state("root.dataset.columnSets", {
	   	     			url: "columnSets",
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/columnSets/templates/views.dataset.columnSets.tpl.html",	   	     			
	   	     			controller: "ColumnSetsViewVM",
	   	     			controllerAs: "ColumnSetsViewVM",
	   	     			resolve:{}
	   	     		})
	   	     		.state("root.dataset.columnSet", {
	   	     			url: "columnSets/{setId}",
	   	     			params: {
	   	     				setId: "new"
	   	     			},
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/columnSets/templates/views.dataset.columnSet.tpl.html",	   	     			
	   	     			controller: "ColumnSetViewVM",
	   	     			controllerAs: "ColumnSetViewVM",
	   	     			resolve:{
	   	     				columnSet: ["$stateParams", "dataset", function($stateParams, dataset){
	   	     					var reset = dataset.resetSelections("column");
	   	     					console.debug("resolve ColumnSet", reset);
	   	     					
	   	     					return reset.$promise.then(function(){
		   	     					var columnSet = _.find(dataset.column.selections, function(selection){
		   	     						return selection.name === $stateParams.setId; 
		   	     					});
		   	     					
		   	     					if(!columnSet){
		   	     						columnSet = {name: "new"};
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