define(['angular', './lib/ViewClass'], function(angular, ViewClass){
	
	return angular.module('Mev.View', ['Mev.Api'])
	.factory('ViewFactory', ['SelectionResourceService',
	 function(SelectionResourceService){
		return {
			get: function(initialData){
				var view = new ViewClass(initialData);
				
				view.selection = SelectionResourceService;
				
				return view;
				
			}
		};
	}])
	
});