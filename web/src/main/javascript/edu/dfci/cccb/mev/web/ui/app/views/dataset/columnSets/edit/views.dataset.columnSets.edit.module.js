define(["mui", "lodash", 
	"../../selectionSets/views.dataset.selectionSets.module"],

function(ng, _, ngColorPicker,
		 template){
	
	var module=ng.module("mui.views.dataset.columnSets.edit", arguments, arguments);
	
	module.config(['$stateProvider', 'SelectionSetResolverProvider', 'SelectionSetsEditOnEnterProvider',
		function($stateProvider, SelectionSetResolverProvider, SelectionSetsEditOnEnterProvider) {

			$stateProvider.state("root.dataset.columnSets.edit", {					
					parent: "root.dataset.columnSets",
					params: {
						setId: undefined
					},
					resolve: {
						selectionSet: SelectionSetResolverProvider.$get()
					},
					onEnter: SelectionSetsEditOnEnterProvider.$get()
				});
		}]);
	return module;
});