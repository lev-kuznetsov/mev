define(['angular',
        './lib/DatasetClass',
        './lib/transcribe', 
        './lib/loadAnalyses',
        './lib/setSelections',
        './lib/resetSelections',
        'api/Api'], 
function(angular, DatasetClass, transcribe, loadAnalyses, setSelections, resetSelections){
	
	return angular.module('Mev.Dataset', ['Mev.Api'])
	.factory('DatasetFactory', ['AnalysisResourceService', 'SelectionResourceService',
	 function(AnalysisResourceService, SelectionResourceService){
	    
	    //DatasetFactory :: [String], [DatasetResponseObj] -> [Dataset]
	    //  Function that takes dataset name and dataset response object and returns
	    //  new dataset.
		return function(datasetName, datasetResponseObj){
		    
		    
				var dataset = new DatasetClass(datasetName, datasetResponseObj);				
		
				dataset.analysis = AnalysisResourceService;
				dataset.selection = SelectionResourceService;
				
				dataset.transcribe = transcribe;

				dataset.loadAnalyses = loadAnalyses;
				dataset.setSelections = setSelections;
				dataset.resetSelections = resetSelections;
				
				return dataset;
				
			};
	}]);
});