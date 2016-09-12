define(["mui", "lodash",
	"./edit/views.dataset.rowSets.edit.module",
	"../selectionSets/views.dataset.selectionSets.module",
	"js/setmanager/SetManager"],
function(ng, _){
	
	var module=ng.module("mui.views.dataset.rowSets", arguments, arguments);
	
	module.config(['$stateProvider', 'SelectionSetResolverProvider',
	   	     	function($stateProvider, SelectionSetResolverProvider){
	   	     		$stateProvider	   	     		
	   	     		.state("root.dataset.rowSets", {
	   	     			url: "rowSets",
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/rowSets/templates/views.dataset.rowSets.tpl.html",	   	     			
	   	     			controller: "SelectionSetsViewVM",
	   	     			controllerAs: "RowSetsViewVM",
	   	     			displayName: "row sets",
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
	   	     			displayName: "{{selectionSet.name}} row set",
	   	     			parent: "root.dataset",
	   	     			templateUrl: "app/views/dataset/rowSets/templates/views.dataset.rowSet.tpl.html",	   	     			
	   	     			controller: "SelectionSetViewVM",
	   	     			controllerAs: "RowSetViewVM",
	   	     			resolve:{
							dimension: function(){
								return "row";
							},
							selectionSet: SelectionSetResolverProvider.$get()
	   	     			}
	   	     		}
	   	     	);
	   	     		
	   	}]);	
	return module;
});