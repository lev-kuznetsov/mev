define(['angular',
        './lib/DatasetClass', 
        './lib/generateView',
        './lib/generateRowFilteredView', 
        'api/Api', 'view/View'], 
function(angular, DatasetClass, generateView, generateRowFilteredView){
	
	return angular.module('Mev.Dataset', ['Mev.Api', 'Mev.View'])
	.factory('DatasetFactory', ['AnalysisResourceService', 'SelectionResourceService',
	                            'ViewFactory',
	 function(AnalysisResourceService, SelectionResourceService, ViewFactory){
		return {
			get: function(initialData){
				var dataset = new DatasetClass(initialData);
				
				dataset.analysis = AnalysisResourceService;
				dataset.selection = SelectionResourceService;
				dataset.view = ViewFactory;

				dataset.addView = generateView;
				dataset.generateRowFilteredView = generateRowFilteredView;
				
				return dataset;
				
			}
		};
	}])
	
});