define(['angular',
        './lib/DatasetClass', 
        './lib/generateView',
        './lib/generateRowFilteredView', 
        'api/Api'], 
function(angular, DatasetClass, generateView, generateRowFilteredView){
	
	return angular.module('Mev.Dataset', ['Mev.Api'])
	.factory('DatasetFactory', ['AnalysisResourceService', 'SelectionResourceService',
	 function(AnalysisResourceService, SelectionResourceService){
		return function(initialData){
				var dataset = new DatasetClass(initialData);
				
				dataset.analysis = AnalysisResourceService;
				dataset.selection = SelectionResourceService;

				dataset.generateView = generateView;
				dataset.generateRowFilteredView = generateRowFilteredView;
				
				return dataset;
				
			}
		
	}])
	
});