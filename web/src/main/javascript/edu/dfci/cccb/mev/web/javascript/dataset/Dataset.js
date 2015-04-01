define(['angular',
        './lib/DatasetClass',
        './lib/loadAnalyses',
        './lib/setSelections',
        './lib/resetSelections',
        'api/Api'], 
function(angular, DatasetClass,loadAnalyses, setSelections, resetSelections){
	
	return angular.module('Mev.Dataset', ['Mev.Api'])
	.factory('DatasetFactory', ['AnalysisResourceService', 'SelectionResourceService', "$q", 'AnalysisEventBus',
	 function(AnalysisResourceService, SelectionResourceService, $q, analysisEventBus){
	    
	    //DatasetFactory :: [String], [DatasetResponseObj] -> [Dataset]
	    //  Function that takes dataset name and dataset response object and returns
	    //  new dataset.
		return function(datasetName, datasetResponseObj){
		    
		    
				var dataset = new DatasetClass(datasetName, datasetResponseObj);				
				
				dataset.analysis = AnalysisResourceService;
				console.debug("api:AnalysisResourceService", AnalysisResourceService, dataset.analysis);
				dataset.selection = SelectionResourceService;

				dataset.$q = $q;
				dataset.analysisEventBus = analysisEventBus;
				
				dataset.loadAnalyses = loadAnalyses;
				dataset.setSelections = setSelections;
				dataset.resetSelections = resetSelections;
				
				return dataset;
				
			};
	}]);
});