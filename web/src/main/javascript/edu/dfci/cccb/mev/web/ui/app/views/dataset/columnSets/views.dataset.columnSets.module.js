define(["mui", "lodash", 
	"./edit/views.dataset.columnSets.edit.module", 
	"../selectionSets/views.dataset.selectionSets.module",
	"js/setmanager/SetManager"],
function(ng, _){
	
	var module=ng.module("mui.views.dataset.columnSets", arguments, arguments);
	
	module.config(['$stateProvider', 'SelectionSetResolverProvider',
	   	     	function($stateProvider, SelectionSetResolverProvider){
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
							dimension: function(){
								return "column";
							},
	   	     				selectionSet: SelectionSetResolverProvider.$get()
	   	     			}
	   	     		}
	   	     	);
	   	     		
	   	}]);	
	return module;
});