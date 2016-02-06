"use strict";
define(['mui',
        './lib/DatasetClass',
        './lib/loadAnalyses',
        './lib/setSelections',
        './lib/resetSelections'], 
function(angular, DatasetClass,loadAnalyses, setSelections, resetSelections){
	

	return angular.module('Mev.Dataset', [])
	//.factory('DatasetFactory', ['AnalysisResourceService', 'SelectionResourceService', "$q", "$http", '$rootScope', 'AnalysisEventBus', "DashboardItems",
	.factory('DatasetFactory', ["$q", "$http", '$rootScope', 
	 function($q, $http, $rootScope, AnalysisResourceService, SelectionResourceService, analysisEventBus, DashboardItems){
	    
	    //DatasetFactory :: [String], [DatasetResponseObj] -> [Dataset]
	    //  Function that takes dataset name and dataset response object and returns
	    //  new dataset.
		return function(datasetName, datasetResponseObj){
		    
		    
				var dataset = new DatasetClass(datasetName, datasetResponseObj, $http, $rootScope);				
				
				dataset.analysis = AnalysisResourceService;
				console.debug("api:AnalysisResourceService", AnalysisResourceService, dataset.analysis);
				dataset.selection = SelectionResourceService;

				dataset.$q = $q;				
				dataset.analysisEventBus = analysisEventBus;
// 				analysisEventBus.onAnalysisSuccess($rootScope, function(analysis){
// //					if(!dataset.analyses)
// //						dataset.analyses = [];
// //					dataset.analyses.push(analysis);
// 				});
				// dataset.dashboardItems = new DashboardItems();
				dataset.loadAnalyses = loadAnalyses;
				dataset.setSelections = setSelections;
				dataset.resetSelections = resetSelections;
				
				return dataset;
				
			};
	}]);
});