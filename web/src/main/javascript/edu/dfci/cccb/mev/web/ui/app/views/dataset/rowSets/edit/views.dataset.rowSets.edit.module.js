define(["mui", "lodash", 
	"../../selectionSets/views.dataset.selectionSets.module"],

function(ng, _, ngColorPicker,
		 template){
	
	var module=ng.module("mui.views.dataset.rowSets.edit", arguments, arguments);
	
	module.config(['$stateProvider', 'SelectionSetResolverProvider', 'SelectionSetsEditOnEnterProvider',
		function($stateProvider, SelectionSetResolverProvider, SelectionSetsEditOnEnterProvider) {

			$stateProvider.state("root.dataset.rowSets.edit", {					
					parent: "root.dataset.rowSets",
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